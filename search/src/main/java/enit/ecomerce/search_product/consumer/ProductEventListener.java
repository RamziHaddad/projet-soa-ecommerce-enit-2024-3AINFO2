package enit.ecomerce.search_product.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.service.ProductService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventListener.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "products-created", containerFactory = "kafkaListenerContainerFactory", groupId = "my-consumer-group")
       public void handleProductListedEvent(ConsumerRecord<String, Object> record) {
    logger.info("Raw event from topic: {}", record.value());
    try {
        if (record.value() == null || !(record.value() instanceof ProductListed)) {
            throw new IllegalArgumentException("Invalid or malformed event: " + record.value());
        }

        ProductListed event = (ProductListed) record.value();
        Product productToAdd = new Product(event);
        productService.createProduct(productToAdd);
        logger.info("Product indexed successfully: {}", productToAdd);
    } catch (Exception e) {
        logger.error("Error processing event from topic 'products-created': {}", e.getMessage(), e);
        // Send to Dead Letter Topic
        sendToDeadLetterTopic("products-created", record, e.getMessage());
    }
}


    @KafkaListener(topics = "products-updated", containerFactory = "kafkaListenerContainerFactory", groupId = "my-consumer-group")
    public void handleProductUpdatedEvent(ConsumerRecord<String, Object> record) {
        try {
           
            if (record.value() == null || !(record.value() instanceof ProductListed)) {
                throw new IllegalArgumentException("Invalid or malformed event: " + record.value());
            }

            ProductListed event = (ProductListed) record.value();
            Product productToUpdate = new Product(event);
            productService.updateProduct(productToUpdate.getId(), productToUpdate);
            logger.info("Product updated successfully: {}", productToUpdate);

        } catch (Exception e) {
            logger.error("Error processing event from topic 'products-updated': {}", e.getMessage(), e);

          
            sendToDeadLetterTopic("products-updated", record, e.getMessage());
        }
    }

    private void sendToDeadLetterTopic(String sourceTopic, ConsumerRecord<String, Object> record, String errorMessage) {
        String deadLetterTopic = "dead-letter-topic";

        try {
            Map<String, Object> deadLetterEvent = new HashMap<>();
            deadLetterEvent.put("sourceTopic", sourceTopic);
            deadLetterEvent.put("partition", record.partition());
            deadLetterEvent.put("offset", record.offset());
            deadLetterEvent.put("key", record.key());
            deadLetterEvent.put("value", record.value());
            deadLetterEvent.put("error", errorMessage);

            // Convert to JSON
            String deadLetterJson = objectMapper.writeValueAsString(deadLetterEvent);

            // Send to Dead Letter Topic
            kafkaTemplate.send(deadLetterTopic, record.key(), deadLetterJson);
            logger.info("Malformed event sent to Dead Letter Topic: {}", deadLetterJson);

        } catch (Exception e) {
            logger.error("Failed to send malformed event to Dead Letter Topic: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "products-created", containerFactory = "kafkaListenerContainerFactory")
    public void handleProductCreatedEvent(String key, ProductListed value) {
        try {
          
            logger.info("Processing product-created event: {}", value);
        } catch (Exception e) {
            logger.error("Error processing product-created event: {}", e.getMessage(), e);
            throw e; 
        }
    }

}
