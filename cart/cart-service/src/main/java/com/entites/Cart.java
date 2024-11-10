package com.entites;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Cart {
    private UUID cartId;
    private Map<UUID, Item> items = new HashMap<>(); // Item ID as key to avoid duplicates

    public Cart(UUID cartId) {
        this.cartId = cartId;
    }

    // Getters and Setters
    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public Map<UUID, Item> getItems() {
        return items;
    }

    public void setItems(Map<UUID, Item> items) {
        this.items = items;
    }
}

