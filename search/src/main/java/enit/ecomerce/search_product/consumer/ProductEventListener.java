package enit.ecomerce.search_product.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.service.ProductService;
@Service
public class ProductEventListener {
     @Autowired
    private ProductService productService;
    @KafkaListener(topics = "products-created", containerFactory = "kafkaListenerContainerFactory" ,groupId = "my-consumer-group")
    public void handleProductListedEvent(ConsumerRecord<String, Object> record) { 
        

            //to do here:before treating check if the event is correctly formated, and check if our product 
                // serrvice indexes the product correclty 
                //no transaction done here for now , maybe we create a saga or an isolated transaction system 

            ProductListed event = (ProductListed) record.value();
            Product productToAdd=new Product(event);
            this.productService.createProduct(productToAdd);
           
       
    }
 
    @KafkaListener(topics = "products-updated", containerFactory = "kafkaListenerContainerFactory" ,groupId = "my-consumer-group")
    public void handleProductUpdatedEvent(ConsumerRecord<String, Object> record) { 
        
        if (record.value() instanceof ProductListed) {
            ProductListed event = (ProductListed) record.value();
            Product productToAdd=new Product(event);
            this.productService.updateProduct(productToAdd.getId(), productToAdd) ;
           
        }
    }
 
}

 