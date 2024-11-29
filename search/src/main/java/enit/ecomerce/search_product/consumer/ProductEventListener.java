package enit.ecomerce.search_product.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import enit.ecomerce.search_product.emitter.EventsEmitter;
import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.product.ProductEntity;
import enit.ecomerce.search_product.repository.ProducteEntityRepository;
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
    private EventsEmitter eventsEmitter; 
    @Autowired 
    private ProducteEntityRepository producteEntityRepository;  

    @KafkaListener(topics = "products-created", containerFactory = "kafkaListenerContainerFactory", groupId = "my-consumer-group")
       public void handleProductListedEvent(ConsumerRecord<String, Object> record) {
    logger.info("Raw event from topic: {}", record.value());
    try {
        if (record.value() == null || !(record.value() instanceof ProductListed)) {
            throw new IllegalArgumentException("Invalid or malformed event: " + record.value()); //we configured a default error handler , we should see how  
                                                                                                //prevent the default handler to execute on certain erreurs
        }
        
        ProductListed event = (ProductListed) record.value();
        if(this.producteEntityRepository.findById(event.aggregateId)!=null){return;} //we already treated the indexation no need to recreate 

        Product productToAdd = new Product(event);
        productService.createProduct(productToAdd);
        logger.info("Product indexed successfully: {}", productToAdd);
        this.producteEntityRepository.save(new ProductEntity(event,true));
    } catch (Exception e) {   // this  takes care of all types of exceptions, should all exceptions 
                                //be sent to the deadletter topic or should we partition them . 
                                // for example if the data base is not connected the erreur will just be sent into 
                                //the dead topic , but we can just treat it withtin an inbox pattern 
                                //maybe we subcribe our selves to the dead letter and treat the exceptions ourselves but oer people could send 
                                //events into this topic ..... maybe we send  to the dead leter only if the erreur is caused 
                                //by sender. 
        ProductListed event = (ProductListed) record.value();
        this.producteEntityRepository.save(new ProductEntity(event,false)); //if the message is malformed , the inbox value can never be treated.
        logger.error("Error processing event from topic 'products-created': {}", e.getMessage(), e);
        eventsEmitter.sendToDeadLetterTopic("products-created", record, e.getMessage());
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

          
            eventsEmitter.sendToDeadLetterTopic("products-updated", record, e.getMessage());
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
