package org.soa.model;

import org.soa.Kafka.dto.ItemDTO;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Cart {
    private UUID cartId;
    private Map<UUID, ItemDTO> items = new LinkedHashMap<>();

    // Getters and setters
    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public Map<UUID, ItemDTO> getItems() {
        return items;
    }

    public void setItems(Map<UUID, ItemDTO> items) {
        this.items = items;
    }
}
