package org.ecommerce.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.ecommerce.domain.Product;
import org.ecommerce.domain.events.ProductPriceUpdateEvent;
import org.ecommerce.exceptions.EntityNotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class PriceUpdateConsumer {
    
    @Inject
    ProductService ps;
    
    @Incoming("product-price-update")
    @Transactional
    public void handlePriceUpdate(String payload) {
        System.out.println("Received payload: " + payload); 
        
        ObjectMapper om = new ObjectMapper();
        try {
            ProductPriceUpdateEvent event = om.readValue(payload, ProductPriceUpdateEvent.class);
            System.out.println("Deserialized Event: " + event); 
            
            Product p = ps.getProductDetails(UUID.fromString(event.getProductId()));
            if (p == null) {
                System.err.println("Product not found for ID: " + event.getProductId());
                return;
            }
            
            p.setShownPrice(event.getPrice());
            ps.updateProduct(p);
            
            System.out.println("Price updated for product: " + p.getProductName() + " to " + p.getShownPrice());
        } catch (JsonMappingException e) {
            System.err.println("Error mapping JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            System.err.println("Error processing JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (EntityNotFoundException e) {
            System.err.println("EntityNotFoundException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
