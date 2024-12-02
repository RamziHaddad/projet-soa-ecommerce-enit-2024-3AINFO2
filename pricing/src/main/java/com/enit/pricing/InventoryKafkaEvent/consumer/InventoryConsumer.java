package com.enit.pricing.InventoryKafkaEvent.consumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.enit.pricing.InventoryKafkaEvent.dto.InventoryEvent;
import com.enit.pricing.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InventoryConsumer {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(InventoryConsumer.class);

    
    public InventoryConsumer(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "product-added", groupId = "pricing-service")
    public void productAdded(InventoryEvent product) {
        logger.info("Received product event ", product.getProductId());
        try{
            productService.addProduct(product.getProductId() , product.getCategory());
        }catch(Exception e){
            logger.error(("Error adding product"), e);
        }
        
    }

     @KafkaListener(topics = "product-updated", groupId = "pricing-service")
    public void productUpdated(InventoryEvent product) {
        logger.info("Received product event", product.getProductId());
        try{
            productService.updateProduct(product.getProductId() ,product.getCategory());
        }catch(Exception e){
            logger.error("Error updating  product", e);
        }
    }

    @KafkaListener(topics = "product-deleted", groupId = "pricing-service")
    public void productDeleted(InventoryEvent product) {
         logger.info("Received product event", product.getProductId());
        try{
            productService.deleteProduct(product.getProductId());
        }catch(Exception e){
            logger.error("Error deleting product", e);
        }
        
    } 
}

