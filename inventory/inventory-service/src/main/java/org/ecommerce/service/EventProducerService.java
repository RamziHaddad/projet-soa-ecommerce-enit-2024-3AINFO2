package org.ecommerce.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.reactive.messaging.annotations.Channel;
import io.smallrye.reactive.messaging.annotations.Emitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PostPersist;
import org.ecommerce.events.MinimalEvent;
import org.ecommerce.events.ProductEvent;
import org.ecommerce.model.Product;

import java.util.UUID;

@ApplicationScoped
public class EventProducerService {
    @Inject
    ObjectMapper objectMapper;

    @Inject
    @Channel("product-events")
    Emitter<String> productEventEmitter;

    @PostPersist
    public void sendProductEvent(Product product, String eventType) throws JsonProcessingException {
        ProductEvent productEvent = new ProductEvent(
                product.getId(),
                product.getTotalQuantity(),
                product.getReservedQuantity(),
                product.getName(),
                product.getCategory(),
                eventType);
        produceProductEvent(productEvent);
    }

    @PostPersist
    public void sendMinimalEvent(UUID idProduct,String eventType) throws JsonProcessingException {
        MinimalEvent productEvent=new MinimalEvent(
                idProduct,
                eventType
        );
        produceMinimalEvent(productEvent);
    }


    @PostPersist
    public void produceProductEvent(ProductEvent productEvent) throws JsonProcessingException {
        try{
            //String jsonEvent = new Gson().toJson(productEvent);
            String productJSON=objectMapper.writeValueAsString(productEvent);
            productEventEmitter.send(productJSON);}
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @PostPersist
    public void produceMinimalEvent(MinimalEvent productEvent) throws JsonProcessingException {
        try{
            //String jsonEvent = new Gson().toJson(productEvent);
            String productJSON=objectMapper.writeValueAsString(productEvent);
            productEventEmitter.send(productJSON);}
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
