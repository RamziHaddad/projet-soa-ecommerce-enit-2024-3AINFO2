package com.enit.pricing.service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enit.pricing.domain.Product;
import com.enit.pricing.domain.ProductPromotion;
import com.enit.pricing.repositories.ProductPromotionRepository;
import com.enit.pricing.repositories.ProductRepository;


@Service
public class ProductPromotionService {

    @Autowired
    private ProductPromotionRepository productPromotionRepository;
    @Autowired
    private ProductRepository productRepository;

 

    public ProductPromotion getProductPromotion(UUID prodId) {
        if (prodId == null) {
            throw new IllegalArgumentException("Product ID null");
        }
        return productPromotionRepository.getPromotionByProduct(prodId);
    }


    public ProductPromotion getPromotionById(UUID promoId) {
        if (promoId == null) {
            throw new IllegalArgumentException("Promotion ID null");
        }
        return productPromotionRepository.findById(promoId);
    }


    //calculates the price after promotion for a product
    public BigDecimal calculateProductPrice( UUID productId){
        Product product= productRepository.findById(productId);
        BigDecimal basePrice= product.getBasePrice();
        ProductPromotion promotion=getProductPromotion(productId);
        if(promotion!=null){
            if(promotion.getReductionPercentage()!= null){
                BigDecimal reductionAmount = basePrice.multiply(promotion.getReductionPercentage()).divide(BigDecimal.valueOf(100));
                BigDecimal finalPrice = basePrice.subtract(reductionAmount);
                productRepository.setCurrentPrice(productId, finalPrice);
                return finalPrice.setScale(3, RoundingMode.HALF_UP);
            }
        }
        return basePrice;
    }

    public ProductPromotion  addProductPromotion(ProductPromotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException("Promotion is null");
        }

            productPromotionRepository.addPromotion(promotion);
            return promotion;
    }


    public void deletePromotion(UUID promoId){
        ProductPromotion promotion = productPromotionRepository.findById(promoId);
        if (promotion!=null) {
            productPromotionRepository.deletePromotionById(promoId);
        }
    }


    public void updateStartDate(UUID promoId, LocalDate startDate) {
        if (promoId == null || startDate == null) {
            throw new IllegalArgumentException("Promotion ID and start date cannot be null");
        }
        productPromotionRepository.updateStartDate(promoId, startDate);
        
    }


    public void updateEndDate(UUID promoId, LocalDate endDate) {
        if (promoId == null || endDate == null) {
            throw new IllegalArgumentException("Promotion ID and end date cannot be null");
        }
        productPromotionRepository.updateEndDate(promoId, endDate);
    }

    public List<ProductPromotion> getActivePromotions(){
        List<ProductPromotion> promotions= productPromotionRepository.getActivePromotions();
        return promotions;
    }

    public void updateReductPerc(UUID promoId, BigDecimal amount) {
        if (promoId == null || amount == null) {
            throw new IllegalArgumentException("Promotion ID and reduction amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0 || amount.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Reduction percentage must be between 0 and 100");
        }
        productPromotionRepository.updateReductPerc(promoId, amount);
    }

    public List<Product> getProductsList(UUID promotionId) {
        if (promotionId == null) {
            throw new IllegalArgumentException("Promotion ID cannot be null");
        }
        return productPromotionRepository.getProducts(promotionId);
    }

    public List<ProductPromotion> getExpiredPromotions(){
        return productPromotionRepository.getExpiredPromotions();    
    }

}
