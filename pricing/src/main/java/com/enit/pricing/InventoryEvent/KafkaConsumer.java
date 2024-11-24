package com.enit.pricing.InventoryEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import com.enit.pricing.service.ProductService;

public class KafkaConsumer {

    private final ProductService productService;
    
    @Autowired
    public KafkaConsumer(ProductService productService) {
        this.productService = productService;
    }


    @KafkaListener(topics = "product-added", groupId = "pricing-service")
    public void productAdded(InventoryEvent product) {
        productService.addProduct(product.getProductId() , product.getCategory());
        System.out.println("Received message: " +product.getCategory());
    }


     @KafkaListener(topics = "product-updated", groupId = "pricing-service")
    public void productUpdated(InventoryEvent product) {
        productService.updateProduct(product.getProductId() ,product.getCategory());
    }

    @KafkaListener(topics = "product-deleted", groupId = "pricing-service")
    public void productDeleted(InventoryEvent product) {
        productService.deleteProduct(product.getProductId());
    } 
}
