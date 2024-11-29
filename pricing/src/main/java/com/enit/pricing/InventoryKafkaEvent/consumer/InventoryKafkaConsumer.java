package com.enit.pricing.InventoryKafkaEvent.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.enit.pricing.InventoryKafkaEvent.dto.InventoryEvent;
import com.enit.pricing.domain.Product;
import com.enit.pricing.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InventoryKafkaConsumer {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(InventoryKafkaConsumer.class);

    
    public InventoryKafkaConsumer(ProductService productService) {
        this.productService = productService;
    }


    @KafkaListener(topics = "product-added", groupId = "pricing-service")
    public void productAdded(InventoryEvent product) {
        //productService.addProduct(product.getProductId() , product.getCategory());
        logger.info("Received message: ");
    }


     @KafkaListener(topics = "product-updated", groupId = "pricing-service")
    public void productUpdated(InventoryEvent product) {
        //productService.updateProduct(product.getProductId() ,product.getCategory());
    }

    @KafkaListener(topics = "product-deleted", groupId = "pricing-service")
    public void productDeleted(InventoryEvent product) {
        //productService.deleteProduct(product.getProductId());
    } 



/*     @KafkaListener(topics = "product-added", groupId = "group-id")
public void consume(String message) {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(message, Product.class);
        logger.info("Consumed: " + product);
    } catch (JsonProcessingException e) {
        System.err.println("Invalid JSON received: " + message);
    }
}
 */
}

