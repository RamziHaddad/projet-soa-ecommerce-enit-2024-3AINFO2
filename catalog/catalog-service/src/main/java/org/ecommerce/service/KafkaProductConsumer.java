package org.ecommerce.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.ecommerce.domain.Product;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.dto.InventoryEvent;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.repository.ProductCategoryRepository;
import org.ecommerce.repository.ProductRepository;

import java.util.UUID;

@ApplicationScoped
public class KafkaProductConsumer {

    @Inject
    ProductService productService;

    @Inject
    ProductCategoryRepository categoryRepo;

    @Inject
    ProductRepository productRepo;

    @Transactional
    @Incoming("products-created")
    public void handleInventoryEvent(String payload) {
        try {
            var objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
            InventoryEvent event = objectMapper.readValue(payload, InventoryEvent.class);

            switch (event.getEventType()) {
                case "CREATE":
                    handleCreateEvent(event);
                    break;
                case "UPDATE":
                    handleUpdateEvent(event);
                    break;
                case "DELETE":
                    handleDeleteEvent(event);
                    break;
                default:
                    System.err.println("Unknown event type: " + event.getEventType());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to process Kafka message: " + payload);
        }
    }

    private void handleCreateEvent(InventoryEvent event) {
        try {
            ProductCategory category = getOrCreateCategory(event.getCategory());
            Product product = new Product(
                    UUID.fromString(event.getProductId()),
                    event.getDescription(),
                    event.getDescription(),
                    java.time.LocalDateTime.now(),
                    category,
                    event.getBasePrice(),
                    event.getBasePrice(),
                    event.isDisponibility()
            );
            productService.add(product, event.getCategory());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to handle create event for product: " + event.getProductId());
        }
    }

    private void handleUpdateEvent(InventoryEvent event) {
        try {
            Product product = productRepo.findById(UUID.fromString(event.getProductId()));
            if (product != null) {
                product.setDescription(event.getDescription());
                product.setBasePrice(event.getBasePrice());
                product.setDisponibility(event.isDisponibility());
                ProductCategory category = getOrCreateCategory(event.getCategory());
                product.setCategory(category);
                productRepo.update(product);
            } else {
                System.err.println("Product not found for update: " + event.getProductId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to handle update event for product: " + event.getProductId());
        }
    }

    private void handleDeleteEvent(InventoryEvent event) {
        try {
            productRepo.delete(UUID.fromString(event.getProductId()));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to handle delete event for product: " + event.getProductId());
        }
    }

    private ProductCategory getOrCreateCategory(String categoryName) {
        ProductCategory category = null;
        try {
            category = categoryRepo.findByName(categoryName);
        } catch (EntityNotFoundException e) {
            category = new ProductCategory(UUID.randomUUID(), categoryName);
            categoryRepo.insert(category);
        }
        return category;
    }
}
