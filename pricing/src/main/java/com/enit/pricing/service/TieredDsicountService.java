package com.enit.pricing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.enit.pricing.domain.TieredDiscount;
import com.enit.pricing.repositories.TieredDiscountRepository;

@Service
public class TieredDsicountService {
    private final TieredDiscountRepository tieredDiscountRepository;

    public TieredDsicountService(TieredDiscountRepository tieredDiscountRepository){
        this.tieredDiscountRepository=tieredDiscountRepository;
    }
    //gets the current discount
    TieredDiscount getCurrentDiscountThresold(){
        Optional<TieredDiscount> discount =tieredDiscountRepository.getCurrentDiscount();
        return discount.orElse(null);
    }
    
    //calculates the total price after the tiered discount
    BigDecimal calculatePriceAfterDiscount(BigDecimal totalPrice){
        TieredDiscount discount = getCurrentDiscountThresold();
        if(discount!=null){
            if(totalPrice .compareTo(discount.getThresholdAmount()) > 0){
                BigDecimal reductionAmount = totalPrice.multiply(discount.getReductionPercentage()).divide(BigDecimal.valueOf(100));
                BigDecimal finalPrice = totalPrice.subtract(reductionAmount);
                return finalPrice.setScale(3, RoundingMode.HALF_UP);
            } 
        }
        return totalPrice;
    }
    
}
