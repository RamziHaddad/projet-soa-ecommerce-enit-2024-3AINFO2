package com.enit.pricing.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ReducThreshold {
    
    UUID promotionId;
    BigDecimal reducThreshold;



    public ReducThreshold(UUID promotionId, BigDecimal reducThreshold) {
        this.promotionId = promotionId;
        this.reducThreshold = reducThreshold;
    }
    public ReducThreshold() {
    }
    public UUID getPromotionId() {
        return promotionId;
    }
    public void setPromotionId(UUID promotionId) {
        this.promotionId = promotionId;
    }
    public BigDecimal getReducThreshold() {
        return reducThreshold;
    }
    public void setReducThreshold(BigDecimal reducThreshold) {
        this.reducThreshold = reducThreshold;
    }
    
}
