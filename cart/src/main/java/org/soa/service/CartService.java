package org.soa.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.soa.dto.ItemDTO;
import org.soa.model.Cart;
import org.soa.model.Item;
import org.soa.messaging.CartPublisher;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CartService {

    @Inject
    RedissonClient redissonClient;

    private static final Logger logger = Logger.getLogger(CartService.class);

    private RMap<UUID, Cart> getCartsMap() {
        return redissonClient.getMap("carts");
    }

    // Créer un nouveau panier
    public Cart createCart(UUID userId) {
        try {
            logger.info("Creating cart for userId: " + userId);
            RMap<UUID, Cart> carts = getCartsMap();

            if (carts.containsKey(userId)) {
                logger.error("Cart already exists for userId: " + userId);
                throw new IllegalStateException("Cart already exists for userId: " + userId);
            }

            Cart cart = new Cart(userId);
            carts.put(userId, cart);
            logger.info("Cart created successfully for userId: " + userId);
            return cart;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating cart: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while creating the cart.", e);
        }
    }

    // Récupérer un panier par userId
    public Cart getCart(UUID userId) {
        try {
            logger.info("Fetching cart for userId: " + userId);
            RMap<UUID, Cart> carts = getCartsMap();

            Cart cart = carts.get(userId);
            if (cart == null) {
                logger.warn("Cart not found for userId: " + userId);
                throw new IllegalArgumentException("Cart not found for userId: " + userId);
            }

            return cart;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching cart: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while fetching the cart.", e);
        }
    }

    // Ajouter un item au panier
    public void addItem(UUID userId, Item item) {
        try {
            logger.info("Adding item to cart for userId: " + userId);
            RMap<UUID, Cart> carts = getCartsMap();
            Cart cart = carts.get(userId);

            if (cart == null) {
                logger.error("Cart not found for userId: " + userId);
                throw new IllegalArgumentException("Cart not found for userId: " + userId);
            }

            cart.getItems().compute(item.getItemId(), (key, existingItem) -> {
                if (existingItem != null) {
                    existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                    return existingItem;
                }
                return item;
            });

            carts.put(userId, cart);
            logger.info("Item added successfully to cart for userId: " + userId);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while adding item: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while adding the item to the cart.", e);
        }
    }

    // Mettre à jour un item dans le panier
    public void updateItem(UUID userId, Item cartItem) {
        try {
            logger.info("Updating item in cart for userId: " + userId);
            RMap<UUID, Cart> carts = getCartsMap();
            Cart cart = carts.get(userId);

            if (cart == null) {
                logger.error("Cart not found for userId: " + userId);
                throw new IllegalArgumentException("Cart not found for userId: " + userId);
            }

            if (!cart.getItems().containsKey(cartItem.getItemId())) {
                logger.error("Item not found in cart for itemId: " + cartItem.getItemId());
                throw new IllegalArgumentException("Item not found in cart for itemId: " + cartItem.getItemId());
            }

            cart.getItems().put(cartItem.getItemId(), cartItem);
            carts.put(userId, cart);
            logger.info("Item updated successfully in cart for userId: " + userId);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating item: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while updating the item in the cart.", e);
        }
    }

    // Supprimer un item du panier
    public void removeItem(UUID userId, UUID itemId) {
        try {
            logger.info("Removing item from cart for userId: " + userId);
            RMap<UUID, Cart> carts = getCartsMap();
            Cart cart = carts.get(userId);

            if (cart == null) {
                logger.error("Cart not found for userId: " + userId);
                throw new IllegalArgumentException("Cart not found for userId: " + userId);
            }

            if (!cart.getItems().containsKey(itemId)) {
                logger.error("Item not found in cart for itemId: " + itemId);
                throw new IllegalArgumentException("Item not found in cart for itemId: " + itemId);
            }

            cart.getItems().remove(itemId);
            carts.put(userId, cart);
            logger.info("Item removed successfully from cart for userId: " + userId);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while removing item: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while removing the item from the cart.", e);
        }
    }

    // Récupérer l'historique des items du panier
    public List<Item> getCartItems(UUID userId) {
        try {
            logger.info("Fetching items from cart for userId: " + userId);
            RMap<UUID, Cart> carts = getCartsMap();
            Cart cart = carts.get(userId);

            if (cart == null) {
                logger.warn("Cart not found for userId: " + userId);
                throw new IllegalArgumentException("Cart not found for userId: " + userId);
            }

            return List.copyOf(cart.getItems().values());
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching cart items: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while fetching the cart items.", e);
        }
    }

    // Supprimer le panier
    public void deleteCart(UUID userId) {
        try {
            logger.info("Deleting cart for userId: " + userId);
            RMap<UUID, Cart> carts = getCartsMap();

            if (!carts.containsKey(userId)) {
                logger.error("Cart not found for userId: " + userId);
                throw new IllegalArgumentException("Cart not found for userId: " + userId);
            }

            carts.remove(userId);
            logger.info("Cart deleted successfully for userId: " + userId);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while deleting cart: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while deleting the cart.", e);
        }
    }

    @Inject
    CartPublisher cartPublisher;

    public void validateCart(UUID cartId) {
        // Récupérer le panier à partir de son ID
        Cart cart = getCart(cartId);
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found with ID: " + cartId);
        }
    
        // Vérifier si le panier est vide
        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot validate an empty cart.");
        }
    
        // Convertir les items du panier en objets ItemDTO
        List<ItemDTO> itemDTOs = cart.getItems().entrySet().stream()
            .map(entry -> new ItemDTO(entry.getKey(), entry.getValue().getName(), 
                                      entry.getValue().getQuantity(), entry.getValue().getPrice()))
            .toList();
    
        // Publier le panier dans le broker
        cartPublisher.publishCart(cartId, itemDTOs);
    
        // Optionnel : journaliser l'action
        logger.info("Cart with ID: " + cartId + " successfully published to the broker.");
    }
    


    
}
