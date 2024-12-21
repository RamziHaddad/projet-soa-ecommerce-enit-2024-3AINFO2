package com.enit.pricing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enit.pricing.dto.CartItem;
@Service
public class PricingService {
    @Autowired
    private  ProductPromotionService productPromotionService;
    private  TieredDsicountService tieredDsicountService;
    //private  ProductService productService; 


  //calculates  the price of the product after applying the corresponding promotion
    public BigDecimal calculateProductPrice(UUID prodId) {
        BigDecimal priceAfterProdPromotion = productPromotionService.calculateProductPrice(prodId);
        return priceAfterProdPromotion;
    }


      //calculates the total of the cart after applying pormotion on each product and before the tiered promotion.
      public BigDecimal calculateCartTotal(List<CartItem> cartItems){
        BigDecimal total= BigDecimal.ZERO;
        for( CartItem c: cartItems){
          BigDecimal productPromoPrice= calculateProductPrice(c.getProductId());
          total = total.add(productPromoPrice.multiply(BigDecimal.valueOf(c.getQuantity())));
        }
        return total;
      }


    //calculates the final total after applying the tiered promotion
     public BigDecimal calculateCartTotalFinal(List<CartItem> cartItems){
        BigDecimal total= calculateCartTotal(cartItems);
        return tieredDsicountService.calculatePriceAfterTieredPromotion(total);
      } 
}