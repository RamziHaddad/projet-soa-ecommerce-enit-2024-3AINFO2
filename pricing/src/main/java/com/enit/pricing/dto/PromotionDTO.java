package com.enit.pricing.dto;

import java.util.UUID;

public class PromotionDTO {
    
    private UUID promotionId;

    public PromotionDTO() {
    }

    public PromotionDTO(UUID promotionId) {
        this.promotionId = promotionId;
    }

    public UUID getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(UUID promotionId) {
        this.promotionId = promotionId;
    }
}
