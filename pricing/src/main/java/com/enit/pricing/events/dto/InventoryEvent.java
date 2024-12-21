package com.enit.pricing.events.dto;

import java.util.UUID;

public class InventoryEvent {

    private UUID productId;

    public InventoryEvent() {
    }

    public InventoryEvent(UUID productId) {
        this.productId = productId;

    }
    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }    
}
