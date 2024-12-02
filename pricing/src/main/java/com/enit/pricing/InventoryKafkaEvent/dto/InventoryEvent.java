package com.enit.pricing.InventoryKafkaEvent.dto;

import java.util.UUID;

public class InventoryEvent {

    private UUID productId;
    private String category;

    public InventoryEvent() {
    }

    public InventoryEvent(UUID productId, String category) {
        this.productId = productId;
        this.category = category;
    }
    
        public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

        public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }    
}
