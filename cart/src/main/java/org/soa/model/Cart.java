package org.soa.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Cart {


    private UUID cartId;
    private Map<UUID, Item> items = new LinkedHashMap<>();

    public Cart() {
    }

    public Cart(UUID cartId) {
        this.cartId = cartId;
    }

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
