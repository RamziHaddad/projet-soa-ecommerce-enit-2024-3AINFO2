package com.enit.pricing.dto;

import java.util.UUID;

public class CartItem {
    private UUID productId;
    private Integer quantity;

    public CartItem(UUID productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
    
    
}
