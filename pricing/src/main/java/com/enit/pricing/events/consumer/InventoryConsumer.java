package com.enit.pricing.events.consumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.enit.pricing.events.dto.InventoryEvent;
import com.enit.pricing.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class InventoryConsumer {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(InventoryConsumer.class);

    @Value("${spring.kafka.topic.inventory-topic}")
    private String addProductTopic;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;


    public InventoryConsumer(ProductService productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = "${spring.kafka.topic.inventory-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void productAdded(InventoryEvent product) {
        logger.info("Received product {}", product.getProductId());
        try{
            productService.addProduct(product.getProductId());
        }catch(Exception e){
            logger.error(("Error adding product"), e);
        }
        
    }

} 
