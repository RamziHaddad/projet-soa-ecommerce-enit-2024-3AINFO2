package org.ecommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.ecommerce.domain.Product;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.domain.events.Event;
import org.ecommerce.domain.events.ProductListed;
import org.ecommerce.domain.events.ProductUpdated;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.repository.ProductRepository;
import org.ecommerce.domain.OutboxEvent;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;

@ApplicationScoped
public class ProductService {

    @Inject
    ProductRepository productRepo;
    @Inject
    ProductCategoryService categoryService;
    @RestClient
    PricingService pricingService;
    @Inject
    @Channel("products-out")
    Emitter<Event> productsListedEmitter;
    @Channel("products-updated")
    Emitter<Event> productsUpdatedEmitter;
    @Inject
    OutboxService outboxService;
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public List<Product> findByRange(int page, int maxResults) {
        return productRepo.findByRange(page, maxResults);
    }

    public List<Product> findAll() {
        return productRepo.findAll();
    }

    public Product getProductDetails(UUID id) throws EntityNotFoundException {
        return productRepo.findById(id);
    }

    public Product add(Product product, String categoryName) throws EntityAlreadyExistsException, EntityNotFoundException {

        product.setId(UUID.randomUUID());
        BigDecimal temp = new BigDecimal("10506");
        product.setBasePrice(temp);
        product.setShownPrice(product.getBasePrice());


        ProductCategory category = categoryService.getCategoryByName(categoryName);
        product.setCategory(category);
        ProductListed productListed = new ProductListed(product);


        try {
            outboxService.createOutboxMessage(productListed);
        }catch (Exception e) {
            logger.error("Failed to create outbox message for product: " + product.getProductName());
            throw new RuntimeException("Failed to create outbox message"); // Handle accordingly
        }


        try {
            CompletionStage<Void> ack = productsListedEmitter.send(productListed);
            ack.thenAccept(result -> {
                logger.info("Product listed and sent via Kafka: " + productListed);
                outboxService.markAsSent(productListed.getEventId());
            }).exceptionally(error -> {
                logger.error("Error when sending the product listed event: " + error.getMessage());
                outboxService.markAsFailed(productListed.getEventId());
                return null;
            });
        } catch (Exception e) {
            logger.error("Error when serializing JSON: " + e.getMessage());
            outboxService.markAsFailed(productListed.getEventId());
        }


        return productRepo.insert(product);
    }

    public Product updateProduct(Product product) throws EntityNotFoundException {
        Product existingProduct = productRepo.findById(product.getId());
    
        if (product.getCategory() != null) {
                ProductCategory newCategory = categoryService.getCategoryByName(product.getCategory().getCategoryName());
                if (newCategory != null) {
                    existingProduct.setCategory(newCategory);
                } else {
                    throw new EntityNotFoundException("Category not found: " + product.getCategory().getCategoryName());
                }
        }
        if (product.getProductName() != null) {
                existingProduct.setProductName(product.getProductName());
        }
        if (product.getDescription() != null) {
                existingProduct.setDescription(product.getDescription());
        }
        if (product.getShownPrice() != null) {
                existingProduct.setShownPrice(product.getShownPrice());
        }
        existingProduct.setDisponibility(product.isDisponibility());
        
        ProductUpdated productUpdated = new ProductUpdated(existingProduct);
        try {
            outboxService.createOutboxMessage(productUpdated);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            logger.error("Failed to create outbox message for product: " + product.getProductName());
            throw new RuntimeException("Failed to create outbox message"); // Handle accordingly
        } catch (EntityAlreadyExistsException e) {
            e.printStackTrace();
            logger.error("Failed to create outbox message for product: " + product.getProductName());
            throw new RuntimeException("Failed to create outbox message"); // Handle accordingly
        }

        try {
            CompletionStage<Void> ack = productsUpdatedEmitter.send(productUpdated);
            ack.thenAccept(result -> {
                logger.info("Product listed and sent via Kafka: " + productUpdated);
                outboxService.markAsSent(productUpdated.getEventId());
            }).exceptionally(error -> {
                logger.error("Error when sending the product listed event: " + error.getMessage());
                outboxService.markAsFailed(productUpdated.getEventId());
                return null;
            });
        } catch (Exception e) {
            logger.error("Error when serializing JSON: " + e.getMessage());
            outboxService.markAsFailed(productUpdated.getEventId());
        }

        return productRepo.update(existingProduct);
    }

    public void removeProduct(UUID id) {
        productRepo.delete(id);
    }
}
