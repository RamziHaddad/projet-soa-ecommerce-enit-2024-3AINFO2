package org.soa.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.soa.Kafka.dto.CartDTO;
import org.soa.Kafka.dto.CartMessageDTO;
import org.soa.Kafka.dto.ItemDTO;
import org.soa.Kafka.messaging.CartProducer;
import org.soa.model.Cart;
import org.soa.model.Item;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class CartService {

    @Inject
    RedissonClient redissonClient;

    @Inject
    CartProducer cartProducer;

    private RMap<UUID, Cart> getCartsMap() {
        return redissonClient.getMap("carts");
    }

    public Cart createCart(UUID userId) {
        RMap<UUID, Cart> carts = getCartsMap();

        if (carts.containsKey(userId)) {
            throw new IllegalStateException("Cart already exists for userId: " + userId);
        }

        Cart cart = new Cart(userId);
        carts.put(userId, cart);

        // Convert Cart to CartDTO and send to Kafka
        CartDTO cartDTO = convertToCartDTO(cart);
        cartProducer.sendCartMessage(new CartMessageDTO(userId, "Cart created", cartDTO));

        return cart;
    }

    public Cart getCart(UUID userId) {
        RMap<UUID, Cart> carts = getCartsMap();
        return carts.get(userId);
    }

    public void addItem(UUID userId, Item item) {
        RMap<UUID, Cart> carts = getCartsMap();
        Cart cart = carts.get(userId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart not found for userId: " + userId);
        }

        cart.getItems().compute(item.getItemId(), (key, existingItem) -> {
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
            } else {
                return item;
            }
            return existingItem;
        });

        carts.put(userId, cart);

        // Convert Cart to CartDTO and send to Kafka
        CartDTO cartDTO = convertToCartDTO(cart);
        cartProducer.sendCartMessage(new CartMessageDTO(userId, "Item added: " + item.getItemId(), cartDTO));
    }

    public void updateItem(UUID userId, Item cartItem) {
        RMap<UUID, Cart> carts = getCartsMap();
        Cart cart = carts.get(userId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart not found for userId: " + userId);
        }

        if (!cart.getItems().containsKey(cartItem.getItemId())) {
            throw new IllegalArgumentException("Item not found in cart for productId: " + cartItem.getItemId());
        }

        cart.getItems().put(cartItem.getItemId(), cartItem);
        carts.put(userId, cart);

        // Convert Cart to CartDTO and send to Kafka
        CartDTO cartDTO = convertToCartDTO(cart);
        cartProducer.sendCartMessage(new CartMessageDTO(userId, "Item updated: " + cartItem.getItemId(), cartDTO));
    }

    public void removeItem(UUID userId, UUID productId) {
        RMap<UUID, Cart> carts = getCartsMap();
        Cart cart = carts.get(userId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart not found for userId: " + userId);
        }

        cart.getItems().computeIfPresent(productId, (key, existingItem) -> {
            if (existingItem.getQuantity() > 1) {
                existingItem.setQuantity(existingItem.getQuantity() - 1);
            } else {
                return null;
            }
            return existingItem;
        });

        carts.put(userId, cart);

        // Convert Cart to CartDTO and send to Kafka
        CartDTO cartDTO = convertToCartDTO(cart);
        cartProducer.sendCartMessage(new CartMessageDTO(userId, "Item removed: " + productId, cartDTO));
    }

    public void clearCart(UUID userId) {
        RMap<UUID, Cart> carts = getCartsMap();
        Cart cart = carts.get(userId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart not found for userId: " + userId);
        }

        cart.getItems().clear();
        carts.put(userId, cart);

        // Convert Cart to CartDTO and send to Kafka
        CartDTO cartDTO = convertToCartDTO(cart);
        cartProducer.sendCartMessage(new CartMessageDTO(userId, "Cart cleared", cartDTO));
    }

    // Helper method to convert Cart to CartDTO
    private CartDTO convertToCartDTO(Cart cart) {
        Map<UUID, ItemDTO> itemDTOs = new LinkedHashMap<>();
        cart.getItems().forEach((id, item) -> {
            ItemDTO itemDTO = new ItemDTO(
                    item.getItemId(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getName(),
                    item.getTotalPrice()
            );
            itemDTOs.put(id, itemDTO);
        });

        return new CartDTO(cart.getUserId(), itemDTOs);
    }
}
