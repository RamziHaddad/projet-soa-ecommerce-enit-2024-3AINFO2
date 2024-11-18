package com.enit.pricing.service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.enit.pricing.domain.ProductPromotion;
import com.enit.pricing.repositories.ProductPromotionRepository;

@Service
public class ProductPromotionService {
    private final ProductPromotionRepository productPromotionRepository;

    public ProductPromotionService(ProductPromotionRepository productPromotionRepository){
        this.productPromotionRepository=productPromotionRepository;
    }


    ProductPromotion getProducDiscount(UUID prodId){
        Optional<ProductPromotion> promotion = productPromotionRepository.getPromotionByProduct(prodId);
        return promotion.orElse(null);
    }



    //calculates the price after promotion for a product
    public BigDecimal calculateProductPrice( UUID productId, BigDecimal basePrice){
        ProductPromotion promotion =getProducDiscount(productId);
        if(promotion!=null){
            if(promotion.getReductionPercentage()!= null){
                BigDecimal reductionAmount = basePrice.multiply(promotion.getReductionPercentage()).divide(BigDecimal.valueOf(100));
                BigDecimal finalPrice = basePrice.subtract(reductionAmount);
                return finalPrice.setScale(3, RoundingMode.HALF_UP);
            }
        }
        return basePrice;
    }
}
