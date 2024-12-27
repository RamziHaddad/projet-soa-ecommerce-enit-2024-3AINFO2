package com.enit.pricing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.enit.pricing.domain.TieredPromotion;
import com.enit.pricing.repositories.TieredDiscountRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TieredDsicountService {
    
    @Autowired
    private  TieredDiscountRepository tieredDiscountRepository;


    public TieredPromotion getPromotionById(UUID promoId) {
        if (promoId == null) {
            throw new IllegalArgumentException("Promotion ID null");
        }
        return tieredDiscountRepository.findById(promoId);
    }


    //calculates the total price after the tiered discount
     public BigDecimal calculatePriceAfterTieredPromotion(BigDecimal totalPrice){
        TieredPromotion discount = getCurrentThrehold();
        if(discount!=null){
            if(totalPrice .compareTo(discount.getThresholdAmount()) > 0){
                BigDecimal reductionAmount = totalPrice.multiply(discount.getReductionPercentage()).divide(BigDecimal.valueOf(100));
                BigDecimal finalPrice = totalPrice.subtract(reductionAmount);
                return finalPrice.setScale(3, RoundingMode.HALF_UP);
            } 
        }
        return totalPrice;
    }


    public TieredPromotion  addProductPromotion(TieredPromotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException("Promotion is null");
        }

            tieredDiscountRepository.addPromotion(promotion);
            return promotion;
    }


    public void deletePromotion(UUID promoId){
        TieredPromotion promotion = tieredDiscountRepository.findById(promoId);
        if (promotion!=null) {
            tieredDiscountRepository.deletePromotionById(promoId);
        }
    }
    
    public void updateThreashold(UUID promoId, BigDecimal threshold) {
        TieredPromotion promotion = tieredDiscountRepository.findById(promoId);
        if (promotion == null) {
            throw new EntityNotFoundException("promotion not found with ID: " + promoId);
        }
        tieredDiscountRepository.updateThreshold(promoId,threshold);
    }   


     public void updateStartDate(UUID promoId, LocalDate startDate) {
        if (promoId == null || startDate == null) {
            throw new IllegalArgumentException("Promotion ID and start date cannot be null");
        }
        tieredDiscountRepository.updateStartDate(promoId, startDate);
        
    }


    public void updateEndDate(UUID promoId, LocalDate endDate) {
        if (promoId == null || endDate == null) {
            throw new IllegalArgumentException("Promotion ID and end date cannot be null");
        }
        tieredDiscountRepository.updateEndDate(promoId, endDate);
    }

    public void updateReductPerc(UUID promoId, BigDecimal amount) {
        if (promoId == null || amount == null) {
            throw new IllegalArgumentException("Promotion ID and reduction amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Reduction percentage must be between 0 and 100");
        }
        tieredDiscountRepository.updateReductPerc(promoId, amount);
    }

 public TieredPromotion getCurrentThrehold(){
    return tieredDiscountRepository.getCurrentThreshold();
 }

}
    

