package org.soa.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.soa.api.CatalogueClient;
import org.soa.dto.ItemDTO;
import org.soa.model.Cart;
import org.soa.model.Item;
import org.soa.messaging.CartPublisher;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class CartService {

    @Inject
    RedissonClient redissonClient;

    @Inject
    @RestClient
    CatalogueClient catalogueClient;

    @Inject
    CartPublisher cartPublisher;

    private static final Logger logger = Logger.getLogger(CartService.class);

    /**
     * Retrieves the map of carts from the Redis cache.
     *
     * @return RMapCache<UUID, Cart> representing the carts.
     */
    private RMapCache<UUID, Cart> getCartsMap() {
        return redissonClient.getMapCache("carts");
    }

    /**
     * Fetches item details from the Catalogue service.
     *
     * @param itemId UUID of the item to fetch details for.
     * @return ItemDTO containing the item details.
     */
    public ItemDTO getItemDetails(UUID itemId) {
        return catalogueClient.fetchItemDetails(itemId);
    }

    /**
     * Creates a new cart for a user.
     *
     * @param userId UUID of the user.
     * @return Cart object representing the newly created cart.
     */
    public Cart createCart(UUID userId) {
        try {
            logger.info("Creating cart for userId: " + userId);

            RMapCache<UUID, Cart> carts = redissonClient.getMapCache("carts");

            if (carts.containsKey(userId)) {
                logger.error("Cart already exists for userId: " + userId);
                throw new IllegalStateException("Cart already exists for userId: " + userId);
            }

            Cart cart = new Cart(userId);
            carts.put(userId, cart, 24, TimeUnit.HOURS); // Cache the cart for 24 hours

            logger.info("Cart created successfully for userId: " + userId);
            return cart;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while creating cart: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while creating the cart.", e);
        }
    }

    /**
     * Retrieves an existing cart by user ID.
     *
     * @param userId UUID of the user.
     * @return Cart object if found.
     */
    public Cart getCart(UUID userId) {
        try {
            logger.info("Fetching cart for userId: " + userId);
            RMapCache<UUID, Cart> carts = getCartsMap();

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

    /**
     * Adds an item to the user's cart by fetching item details from the catalogue.
     *
     * @param userId    UUID of the user.
     * @param productId UUID of the product to add.
     * @param quantity  Quantity of the product to add.
     * @return Item object representing the added item.
     */
    public Item addItemFromCatalog(UUID userId, UUID productId, int quantity) {
        ItemDTO product = catalogueClient.fetchItemDetails(productId);

        if (product == null) {
            throw new IllegalArgumentException("Produit non trouvé pour l'ID : " + productId);
        }

        RMapCache<UUID, Cart> carts = getCartsMap();
        Cart cart = carts.get(userId);

        Item newItem = new Item(productId, quantity, product.getPrice(), product.getName());
        cart.getItems().put(newItem.getItemId(), newItem);
        carts.put(userId, cart);
        return newItem;
    }

    /**
     * Updates an item in the user's cart.
     *
     * @param userId   UUID of the user.
     * @param cartItem Item object with updated details.
     */
    public void updateItem(UUID userId, Item cartItem) {
        try {
            logger.info("Updating item in cart for userId: " + userId);
            RMapCache<UUID, Cart> carts = getCartsMap();
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

    /**
     * Removes an item from the user's cart.
     *
     * @param userId UUID of the user.
     * @param itemId UUID of the item to remove.
     */
    public void removeItem(UUID userId, UUID itemId) {
        try {
            logger.info("Removing item from cart for userId: " + userId);
            RMapCache<UUID, Cart> carts = getCartsMap();
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

    /**
     * Retrieves all items from the user's cart.
     *
     * @param userId UUID of the user.
     * @return List of Item objects in the cart.
     */
    public List<Item> getCartItems(UUID userId) {
        try {
            logger.info("Fetching items from cart for userId: " + userId);
            RMapCache<UUID, Cart> carts = getCartsMap();
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

    /**
     * Deletes the user's cart.
     *
     * @param userId UUID of the user.
     */
    public void deleteCart(UUID userId) {
        try {
            logger.info("Deleting cart for userId: " + userId);
            RMapCache<UUID, Cart> carts = getCartsMap();

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

    /**
     * Clears all items in the user's cart.
     *
     * @param userId UUID of the user.
     */
    public void clearCart(UUID userId) {
        try {
            logger.info("Clearing cart for userId: " + userId);
            RMapCache<UUID, Cart> carts = getCartsMap();

            if (!carts.containsKey(userId)) {
                logger.error("Cart not found for userId: " + userId);
                throw new IllegalArgumentException("Cart not found for userId: " + userId);
            }

            Cart cart = carts.get(userId);
            cart.getItems().clear();
            carts.put(userId, cart);

            logger.info("Cart cleared successfully for userId: " + userId);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while clearing cart: " + e.getMessage(), e);
            throw new RuntimeException("An unexpected error occurred while clearing the cart.", e);
        }
    }

    /**
     * Validates and publishes the user's cart to the messaging broker.
     *
     * @param cartId UUID of the cart to validate.
     */
    public void validateCart(UUID cartId) {
        Cart cart = getCart(cartId);
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found with ID: " + cartId);
        }

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cannot validate an empty cart.");
        }

        List<ItemDTO> itemDTOs = cart.getItems().entrySet().stream()
                .map(entry -> new ItemDTO(entry.getKey(), entry.getValue().getName(),
                        entry.getValue().getQuantity(), entry.getValue().getPrice()))
                .toList();

        cartPublisher.publishCart(cartId, itemDTOs);
        logger.info("Cart with ID: " + cartId + " successfully published to the broker.");
    }
}
