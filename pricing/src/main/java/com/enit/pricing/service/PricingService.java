package com.enit.pricing.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enit.pricing.dto.CartItem;
@Service
public class PricingService {
    private final ProductPromotionService productPromotionService;
    private final TieredDsicountService tieredDsicountService;
    private final ProductService productService;

    @Autowired
    public PricingService(ProductPromotionService productPromotionService,
                          TieredDsicountService tieredDsicountService,
                          ProductService productService){
                            this.productPromotionService= productPromotionService;
                            this.tieredDsicountService=tieredDsicountService;
                            this.productService=productService;
                          }

     /**
     * This function calculates the price of a product after applying promotions to it.
     * To calculate the final price with two promotions applied at the same time, 
     * we first apply the first promotion to the base price, then apply the second promotion 
     * to the resulting price. ->  both promotions are factored in sequentially.
     */
    @SuppressWarnings("unused")
    public BigDecimal calculateProductPrice(UUID prodId) {
        BigDecimal basePrice = productService.getProductBasePrice(prodId);
        BigDecimal priceAfterProdPromotion = productPromotionService.calculateProductPrice(prodId, basePrice);
        return priceAfterProdPromotion;
    }

    // assuming that prodId and Qte are also stored in a map. ( we can find another way to store qte, category and idProd later)
    @SuppressWarnings("unused")
    public BigDecimal calculateCartTotalFinal(List<CartItem> cartItems){
        BigDecimal total= calculateCartTotal(cartItems);
        return tieredDsicountService.calculatePriceAfterDiscount(total);
      }

      
      public BigDecimal calculateCartTotal(List<CartItem> cartItems){
        BigDecimal total= BigDecimal.ZERO;
        for( CartItem c: cartItems){
          BigDecimal productPromoPrice= calculateProductPrice(c.getProductId());
          total = total.add(productPromoPrice.multiply(BigDecimal.valueOf(c.getQuantity())));
        }
        return total;
      }
}