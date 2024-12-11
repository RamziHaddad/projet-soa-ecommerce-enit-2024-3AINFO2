package org.soa.Kafka.dto;

import java.util.Map;
import java.util.UUID;

public class CartDTO {
    private UUID cartId;
    private Map<UUID, ItemDTO> items;

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
