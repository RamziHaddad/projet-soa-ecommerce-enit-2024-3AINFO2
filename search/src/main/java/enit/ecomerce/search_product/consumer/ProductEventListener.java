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
    private ProducteEntityRepository producteEntityRepository;

    @KafkaListener(topics = "products-created", containerFactory = "kafkaListenerContainerFactory", groupId = "my-consumer-group")
    public void handleProductListedEvent(ConsumerRecord<String, Object> record) {
        logger.info("Raw event from topic: [topic: {}, partition: {}, offset: {}, key: {}, value: {}]",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
   
            ProductListed event = (ProductListed) record.value();

 
            if (this.producteEntityRepository.findById(event.aggregateId).isPresent()) {
                logger.info("Product with ID {} is already indexed. Skipping.", event.aggregateId);
                return;
            }

            
            Product productToAdd = new Product(event);
            productService.createProduct(productToAdd);
            logger.info("Product indexed successfully: {}", productToAdd);
 
            this.producteEntityRepository.save(new ProductEntity(event, true));

       
    } 

    

    @KafkaListener(topics = "products-updated", containerFactory = "kafkaListenerContainerFactory", groupId = "my-consumer-group")
    public void handleProductUpdatedEvent(ConsumerRecord<String, Object> record) {
        logger.info("Raw event from topic: [topic: {}, partition: {}, offset: {}, key: {}, value: {}]",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());
    
            ProductListed event = (ProductListed) record.value();
            Product productToUpdate = new Product(event);
            productService.updateProduct(productToUpdate.getId(), productToUpdate);
            logger.info("Product updated successfully: {}", productToUpdate);
 

 
}
}