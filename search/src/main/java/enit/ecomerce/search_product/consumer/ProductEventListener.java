package enit.ecomerce.search_product.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import enit.ecomerce.search_product.emitter.EventsEmitter;
import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.product.ProductEntity;
import enit.ecomerce.search_product.repository.ProducteEntityRepository;
import enit.ecomerce.search_product.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventListener.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private EventsEmitter eventsEmitter;

    @Autowired
    private ProducteEntityRepository producteEntityRepository;

    @KafkaListener(topics = "products-created", containerFactory = "kafkaListenerContainerFactory", groupId = "my-consumer-group")
    public void handleProductListedEvent(ConsumerRecord<String, Object> record) {
        logger.info("Raw event from topic: [topic: {}, partition: {}, offset: {}, key: {}, value: {}]",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
        try {
            // Validate the record
            if (!(record.value() instanceof ProductListed)) {
                throw new IllegalArgumentException("Invalid or malformed event: " + record.value());
            }

            ProductListed event = (ProductListed) record.value();

            // Check if the product has already been indexed
            if (this.producteEntityRepository.findById(event.aggregateId).isPresent()) {
                logger.info("Product with ID {} is already indexed. Skipping.", event.aggregateId);
                return;
            }

            // Process and index the product
            Product productToAdd = new Product(event);
            productService.createProduct(productToAdd);
            logger.info("Product indexed successfully: {}", productToAdd);

            // Save successful processing in the repository
            this.producteEntityRepository.save(new ProductEntity(event, true));

        } catch (IllegalArgumentException e) {
            // Handle validation exceptions
            logger.error("Validation error: {}", e.getMessage());
            sendToDeadLetterTopic(record, "Validation Error", e);

        } catch (DataAccessException e) {
            logger.error("Database error: {}", e.getMessage());
            retryOrSendToInbox(record, e);
        } catch (Exception e) {
            // Handle other unexpected exceptions
            logger.error("Unexpected error while processing event: {}", e.getMessage(), e);
            sendToDeadLetterTopic(record, "Unexpected Error", e);
        }
    }

    @KafkaListener(topics = "products-updated", containerFactory = "kafkaListenerContainerFactory", groupId = "my-consumer-group")
    public void handleProductUpdatedEvent(ConsumerRecord<String, Object> record) {
        logger.info("Raw event from topic: [topic: {}, partition: {}, offset: {}, key: {}, value: {}]",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
        try {
            // Validate the record
            if (!(record.value() instanceof ProductListed)) {
                throw new IllegalArgumentException("Invalid or malformed event: " + record.value());
            }

            ProductListed event = (ProductListed) record.value();

            // Update the product
            Product productToUpdate = new Product(event);
            productService.updateProduct(productToUpdate.getId(), productToUpdate);
            logger.info("Product updated successfully: {}", productToUpdate);

        } catch (IllegalArgumentException e) {
            // Handle validation exceptions
            logger.error("Validation error: {}", e.getMessage());
            sendToDeadLetterTopic(record, "Validation Error", e);

        } catch (DataAccessException e) {
            logger.error("Database error: {}", e.getMessage());
            retryOrSendToInbox(record, e);
        } catch (Exception e) {
            // Handle other unexpected exceptions
            logger.error("Unexpected error while processing event: {}", e.getMessage(), e);
            sendToDeadLetterTopic(record, "Unexpected Error", e);
        }
    }

    private void retryOrSendToInbox(ConsumerRecord<String, Object> record, Exception exception) {
        try {
            // Retry logic (if needed) or directly save to the inbox
            logger.info("Saving to inbox due to failure: {}", record.value());

            if (record.value() instanceof ProductListed) {
                ProductListed event = (ProductListed) record.value();

                ProductEntity productEntity = new ProductEntity(event, false);
                producteEntityRepository.save(productEntity);
                logger.info("Record saved to inbox successfully: {}", productEntity);
            } else {
                logger.error("Cannot save malformed record to inbox: {}", record.value());
                sendToDeadLetterTopic(record, "Invalid Record for Inbox", exception);
            }
        } catch (Exception e) {
            // If saving to inbox also fails, send to dead letter topic
            logger.error("Failed during retry/inbox handling: {}", e.getMessage(), e);
            sendToDeadLetterTopic(record, "Retry/Inbox Failure", e);
        }
    }

    private void sendToDeadLetterTopic(ConsumerRecord<String, Object> record, String errorMessage,
            Exception exception) {
        try {
            logger.error("Sending record to dead-letter topic. Error: {}", errorMessage);
            eventsEmitter.sendToDeadLetterTopic(record.topic(), record, errorMessage);
        } catch (Exception e) {
            logger.error("Failed to send record to dead-letter topic: {}", e.getMessage(), e);
        }
    }
}
