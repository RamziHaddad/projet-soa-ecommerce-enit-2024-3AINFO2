package org.soa.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.soa.model.Cart;
import org.soa.model.Item;

import java.util.UUID;

@ApplicationScoped
public class CartService {

    @Inject
    RedissonClient redissonClient;

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
                return existingItem;
            }
            return item;
        });

        carts.put(userId, cart); // Mise à jour explicite
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
        carts.put(userId, cart); // Mise à jour explicite
    }

    public void removeItem(UUID userId, UUID productId) {
        RMap<UUID, Cart> carts = getCartsMap();
        Cart cart = carts.get(userId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart not found for userId: " + userId);
        }

        cart.getItems().remove(productId);
        carts.put(userId, cart); // Mise à jour explicite
    }

    public void clearCart(UUID userId) {
        RMap<UUID, Cart> carts = getCartsMap();
        Cart cart = carts.get(userId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart not found for userId: " + userId);
        }

        cart.getItems().clear();
        carts.put(userId, cart); // Mise à jour explicite
    }
}
