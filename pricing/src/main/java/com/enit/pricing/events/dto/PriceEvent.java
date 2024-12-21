package com.enit.pricing.events.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class PriceEvent {
    private UUID productId;
    private BigDecimal price;

    
    public PriceEvent() {
    }
    public PriceEvent(UUID productId, BigDecimal price) {
        this.productId = productId;
        this.price = price;
    }
    public UUID getProductId() {
        return productId;
    }
    public void setProductId(UUID productId) {
        this.productId = productId;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
} 
