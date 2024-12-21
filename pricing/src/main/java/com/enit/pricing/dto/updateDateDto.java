package com.enit.pricing.dto;

import java.time.LocalDate;
import java.util.UUID;

public class UpdateDateDto {

    private UUID promotionId;
    private LocalDate date;

    public UpdateDateDto() {
    }
    public UpdateDateDto(UUID promotionId, LocalDate date) {
        this.promotionId = promotionId;
        this.date = date;
    }
    public UUID getPromotionId(){
        return promotionId;
    }
    public void setPromotionId(UUID promotionId) {
        this.promotionId = promotionId;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setdDte(LocalDate date) {
        this.date = date;
    }
    
}
