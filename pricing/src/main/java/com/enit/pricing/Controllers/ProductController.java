package com.enit.pricing.Controllers;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enit.pricing.domain.Product;
import com.enit.pricing.dto.PromotionDTO;
import com.enit.pricing.events.dto.InventoryEvent;
import com.enit.pricing.events.dto.PriceEvent;
import com.enit.pricing.events.producer.PriceUpdateProducer;
import com.enit.pricing.service.PricingService;
import com.enit.pricing.service.ProductService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@SpringBootApplication
@RequestMapping("/product")
public class ProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private PriceUpdateProducer priceProducer;
    @Autowired
    private PricingService pricingService;



    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@RequestBody InventoryEvent event) {
        try {
            Product product = productService.addProduct(event.getProductId());
            return ResponseEntity.status(HttpStatus.CREATED).body(product); 
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); 
        }
    }

    @GetMapping("getProduct/{prodId}")
    public ResponseEntity <Product> getProduct(@PathVariable("prodId") UUID prodId) {
        try{
            Product product = productService.getProduct(prodId);
            return ResponseEntity.ok()
                                 .header("message","Product updated")
                                 .body(product);

        }catch(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .header("message", "Product with Id "+prodId +" not found")
                             .build();
        }
    }

    @DeleteMapping("deleteProduct/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("prodId") UUID prodId){
        try{
            productService.deleteProduct(prodId);
            return ResponseEntity.ok("Product with ID "+prodId+ " deleted");
        }
        catch(EntityNotFoundException e){
            return ResponseEntity.ok("Product with ID "+prodId+ " not found");
        }  
    }

     @PutMapping("/updateProduct")
    public ResponseEntity<Product> updateProduct(@RequestBody InventoryEvent event) {
        try{
            Product Product= productService.updateProduct(event.getProductId());
            return ResponseEntity.ok()
                                 .header("message","Product updated")
                                 .body(Product);

        }catch(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .header("message", "Product with Id "+event.getProductId() +" not found")
                             .build();
        }
    }

    //add a promotion to a product
    @PutMapping("addPromotiontoProduct/{prodId}")
    public ResponseEntity<PriceEvent> addPromotion(@PathVariable UUID prodId, @RequestBody PromotionDTO promotion) {
        try{
            productService.addPromotiontoProduct(prodId,promotion.getPromotionId());
            BigDecimal prodPrice= pricingService.calculateProductPrice(prodId);
            PriceEvent event= new PriceEvent(prodId,prodPrice);
            priceProducer.sendPrice(event);
            return ResponseEntity.ok()
                                .header("message","Promotion added and event sent")
                                .body(event);
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .header("message", "Product with Id "+prodId+" not found")
                                .build();
        }

    }
    //for setting base price ( after a product sent by the inventory is added the base price will be set from here)
    @PutMapping("basePrice/{prodId}")
    public ResponseEntity<String> setBasePrice(@PathVariable UUID prodId, @RequestBody PriceEvent price) {
        try{
            productService.updateBasePrice(prodId, price.getPrice());
            return ResponseEntity.ok().body("Price updated successfully");
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Product with Id "+prodId+" not found");
        }
    }

    @DeleteMapping("deletePromotion/{prodId}")
    public ResponseEntity<String> deletePromoFromProduct(@PathVariable("prodId") UUID prodId){
        try{
            productService.updateProductCurrentPrice(prodId,productService.getBasePrice(prodId));
            PriceEvent event= new PriceEvent(prodId,productService.getBasePrice(prodId));
            priceProducer.sendPrice(event);
            productService.removePromotionFromProduct(prodId);
            return ResponseEntity.ok("Product with ID "+prodId+ " deleted");
        }
        catch(EntityNotFoundException e){
            return ResponseEntity.ok("Product with ID "+prodId+ " not found");
        }  
    }
    
}
