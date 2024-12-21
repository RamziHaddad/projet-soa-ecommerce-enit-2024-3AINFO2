package com.enit.pricing.scheduling;

import com.enit.pricing.service.ProductPromotionService;
import com.enit.pricing.domain.ProductPromotion;
import com.enit.pricing.domain.Product;
import com.enit.pricing.service.ProductService;
import com.enit.pricing.events.dto.PriceUpdateEvent;
import com.enit.pricing.events.producer.PriceUpdateProducer;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;


import java.util.List;

@Component
public class ExpiredPromoScheduler {

    @Autowired
    ProductPromotionService productPromotionService;

    @Autowired
    ProductService productService;

    @Autowired
    PriceUpdateProducer priceProducer;


    //@Scheduled(fixedRate = 60000)  //every minute
    @Scheduled(cron = "0 0 0 * * ?") // checks once a day at midnight
    @Transactional
    public void checkExcpiredPromotion() {

        List<ProductPromotion> expiredPromotions = productPromotionService.getExpiredPromotions();
        for (ProductPromotion promotion : expiredPromotions) {
            processExpiredPromotion(promotion);
        }
    }
    private void processExpiredPromotion(ProductPromotion promotion) {
    List<Product> products = promotion.getProducts(); 
        for (Product p : products) {
            productService.removePromotionFromProduct(p.getProductId());
            productService.updateProductCurrentPrice(p.getProductId(), p.getBasePrice());
            PriceUpdateEvent event= new PriceUpdateEvent(p.getProductId(),p.getBasePrice());
            priceProducer.sendPrice(event);
        }
    }
    
}
