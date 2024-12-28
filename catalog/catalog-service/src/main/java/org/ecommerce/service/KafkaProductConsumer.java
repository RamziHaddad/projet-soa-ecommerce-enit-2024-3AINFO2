package org.ecommerce.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.ecommerce.domain.Product;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.domain.events.InventoryEvent;
import org.ecommerce.domain.events.ProductAvailabilityEvent;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

@ApplicationScoped
public class KafkaProductConsumer {

    @Inject
    ProductService productService;

    @Inject
    ProductCategoryService categoryService;

    @Inject
    ProductRepository productRepo;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    @Incoming("product-events")
    public void handleInventoryEvent(String payload) {
        try {
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

            String categoryName = event.getCategory().getName();
            ProductCategory category = getOrCreateCategory(categoryName);

            Product product = new Product(
                    UUID.fromString(event.getProductId()),
                    event.getName(),
                    event.getDescription(),
                    java.time.LocalDateTime.now(),
                    category,
                    null,
                    null,
                    event.isDisponibility()
            );

            productService.add(product, categoryName);

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
                ProductCategory category = getOrCreateCategory(event.getCategory().getName());  // Adjusted to use DTO
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
        ProductCategory category;
        try {
            category = categoryService.getCategoryByName(categoryName);
        } catch (EntityNotFoundException e) {
            category = new ProductCategory();
            category.setCategoryName(categoryName);
            try {
                categoryService.addCategory(category);
            } catch (EntityAlreadyExistsException e1) {
                e1.printStackTrace();
            }
        }
        return category;
    }

    @Transactional
    @Incoming("product-availability")
    public void updateAvailabilityOfProduct(String payload) {
        try {
            ProductAvailabilityEvent event = objectMapper.readValue(payload, ProductAvailabilityEvent.class);
            UUID productId = UUID.fromString(event.getProductId());
            Product product = productService.getProductDetails(productId);

            if (product != null) {
                boolean isAvailable = "IN_STOCK".equals(event.getAvailability());
                product.setDisponibility(isAvailable);
                productService.updateProduct(product);
                System.out.println("Product availability updated: " + product.getId() + " to " + isAvailable);
            } else {
                System.err.println("Product not found for availability update: " + productId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to process product availability event: " + payload);
        }
    }
}
