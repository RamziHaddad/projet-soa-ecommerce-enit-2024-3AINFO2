package com.enit.pricing.InventoryKafkaEvent.dto;

import java.util.UUID;

public class InventoryEvent {

    private int productId;
    private String category;

    public InventoryEvent() {
    }

    
    public InventoryEvent(int productId, String category) {
        this.productId = productId;
        this.category = category;
    }
    
        public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

        public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
        @Override
    public String toString() {
        return "InventoryEvent [productId=" + productId + ", category=" + category + "]";
    }


    
}
