package enit.ecomerce.search_product.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import enit.ecomerce.search_product.product.Product;
import enit.ecomerce.search_product.repository.ProductRepository;
@Component
public class MyMessageListener{

    @Autowired
    private ProductRepository productRepository;

    @KafkaListener(id = "producCreation", topics = "product.created")
        public void productCreated(ConsumerRecord<?, ProductReceived> record) { 
            Product produit=new Product(record.value()); 
            this.productRepository.save(produit);  
    } 

    @KafkaListener(id = "producDeletion", topics = "product.deleted")
        public void productDeleted(ConsumerRecord<?, ProductReceived> record) { 
            Product produit=new Product(record.value()); 
            this.productRepository.delete(produit); 
    }
    @KafkaListener(id = "productUpdate", topics = "product.updated")
    public void productUpdated(ConsumerRecord<?, ProductReceived> record) { 
        Product produit=new Product(record.value()); 
        this.productRepository.save(produit);
}
}