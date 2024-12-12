package org.soa.dto;

import java.util.Map;
import java.util.UUID;

import org.soa.model.Item;

public class CartMessage {
    private UUID cartId;
    private Map<UUID, Item> items;

    public CartMessage() {}

    public CartMessage(UUID cartId, Map<UUID, Item> items) {
        this.cartId = cartId;
        this.items = items;
    }

    public UUID getCartId() {
        return cartId;
    }

    public Map<UUID, Item> getItems() {
        return items;
    }
}
