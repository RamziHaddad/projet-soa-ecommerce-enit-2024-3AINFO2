package com.enit.pricing.Controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enit.pricing.domain.Product;
import com.enit.pricing.domain.ProductPromotion;
import com.enit.pricing.dto.ReducThreshold;
import com.enit.pricing.events.dto.PriceEvent;
import com.enit.pricing.events.producer.PriceUpdateProducer;
import com.enit.pricing.service.PricingService;
import com.enit.pricing.service.ProductPromotionService;
import com.enit.pricing.service.ProductService;
import com.enit.pricing.dto.UpdateDateDto;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;


@SpringBootApplication
@RestController
@RequestMapping("/productPromotion")
public class ProductPromotionController {

    @Autowired
    ProductPromotionService productPromotionService;
    @Autowired
    PricingService pricingService;
    @Autowired ProductService productService;

    @Autowired
    PriceUpdateProducer priceProducer;

    @PostMapping("/addPromotion")
    public ResponseEntity<ProductPromotion> addPromotion(@RequestBody ProductPromotion promotion) {
            productPromotionService.addProductPromotion(promotion);
            return ResponseEntity.status(HttpStatus.CREATED).body(promotion);
       
    }

 
    @DeleteMapping("/deletePromotion/{promotionId}")
    public ResponseEntity<String> deletePromotion(@PathVariable UUID promotionId){
        try{
            List<Product> products= productPromotionService.getProductsList(promotionId);
            for(Product p: products){     
                productService.updateProductCurrentPrice(p.getProductId(), p.getBasePrice());
                PriceEvent event= new PriceEvent(p.getProductId(),p.getBasePrice());
                priceProducer.sendPrice(event);
            } 
            productPromotionService.deletePromotion(promotionId);
            return ResponseEntity.ok("Promotion with ID "+promotionId+ " deleted");
            }catch(EntityNotFoundException e){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Promotion with ID "+promotionId+ " not found");
            }    
    }

    @PutMapping("/startDate")
    public  ResponseEntity<String> updateStartDate(@RequestBody UpdateDateDto date){
        try{
            productPromotionService.updateStartDate(date.getPromotionId(), date.getDate());
            return ResponseEntity.ok("Start date updated successfully");
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Promotion with ID "+date.getPromotionId()+ " not found");
        }
    }

    @PutMapping("/endDate")
    public  ResponseEntity<String> updateEndDate(@RequestBody UpdateDateDto date){
        try{
            productPromotionService.updateEndDate(date.getPromotionId(), date.getDate());
            return ResponseEntity.ok("End date updated successfully");
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Promotion with ID "+date.getPromotionId()+ " not found");
        }
    }

    
    @GetMapping("/prodPromotion/{productId}")
    public ResponseEntity<ProductPromotion> getProductPromotion(@PathVariable UUID productId) {
        try{
            ProductPromotion promotion= productPromotionService.getProductPromotion(productId);
            return ResponseEntity.ok()
                                 .header("message","No promotions for product"+productId)
                                 .body(promotion);
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .header("message","promotion for product "+ productId +" not found")
                                 .build();
        }
    }


    @GetMapping("/getPromotion/{promotionId}")
    public ResponseEntity<ProductPromotion> getPromotionById(@PathVariable UUID promotionId) {
        try{
            ProductPromotion promotion= productPromotionService.getPromotionById(promotionId);
            return ResponseEntity.ok()
                                 .header("message","Success")
                                 .body(promotion);
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .header("message","promotion with ID "+ promotionId +" not found")
                                 .build();
        }
    }


    @GetMapping("/activePromotions")
    public  ResponseEntity<List<ProductPromotion>> getActivePromotions() {
        try{
            List<ProductPromotion> productPromotions=productPromotionService.getActivePromotions();
            return ResponseEntity.ok(productPromotions);
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .header("message","No active promotions")
                                 .build();
        }

    }

        @GetMapping("/expiredPromotions")
    public  ResponseEntity<List<ProductPromotion>> getExpiredPromotions() {
        try{
            List<ProductPromotion> productPromotions=productPromotionService.getExpiredPromotions();
            return ResponseEntity.ok(productPromotions);
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .header("message","No expired promotions")
                                 .build();
        }

    }

    @PutMapping("/updateReductionPer")
    public ResponseEntity<String> updateReductPerc(@RequestBody ReducThreshold value) {
        try {
            productPromotionService.updateReductPerc(value.getPromotionId(), value.getReducThreshold());

            List<Product> products= productPromotionService.getProductsList(value.getPromotionId());
            for(Product p: products){     
                UUID prodId= p.getProductId();
                BigDecimal prodPrice= pricingService.calculateProductPrice(prodId);
                PriceEvent event= new PriceEvent(prodId,prodPrice);
                priceProducer.sendPrice(event);
            }

            return ResponseEntity.ok("Reduction percentage updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating Reduction percentage: " + e.getMessage());
        }
    }

}
