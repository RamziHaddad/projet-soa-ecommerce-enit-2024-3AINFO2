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

// This class consumes Kafka messages related to product inventory and availability.
@ApplicationScoped
public class KafkaProductConsumer {

    // Injecting ProductService to handle product-related operations
    @Inject
    ProductService productService;

    // Injecting ProductCategoryService to manage product categories
    @Inject
    ProductCategoryService categoryService;

    // Injecting ProductRepository for database operations on products
    @Inject
    ProductRepository productRepo;

    // Jackson ObjectMapper for converting JSON strings to Java objects
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Consumes messages from the "product-events" Kafka topic and processes inventory events
    @Transactional
    @Incoming("product-events")
    public void handleInventoryEvent(String payload) {
        try {
            // Deserialize the JSON payload into an InventoryEvent object
            InventoryEvent event = objectMapper.readValue(payload, InventoryEvent.class);

            // Process the event based on its type
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

    // Handles the creation of new products based on the InventoryEvent
    private void handleCreateEvent(InventoryEvent event) {
        try {
            // Retrieve or create the product category
            String categoryName = event.getCategory().getName();
            ProductCategory category = getOrCreateCategory(categoryName);

            // Create a new Product instance using data from the event
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

            // Add the new product to the system
            productService.add(product, categoryName);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to handle create event for product: " + event.getProductId());
        }
    }

    // Handles updates to existing products based on the InventoryEvent
    private void handleUpdateEvent(InventoryEvent event) {
        try {
            // Find the product by its ID
            Product product = productRepo.findById(UUID.fromString(event.getProductId()));
            if (product != null) {
                // Update product details based on the event
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

    // Handles the deletion of products based on the InventoryEvent
    private void handleDeleteEvent(InventoryEvent event) {
        try {
            // Delete the product by its ID
            productRepo.delete(UUID.fromString(event.getProductId()));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to handle delete event for product: " + event.getProductId());
        }
    }

    // Retrieves an existing category by name or creates a new one if it doesn't exist
    private ProductCategory getOrCreateCategory(String categoryName) {
        ProductCategory category;
        try {
            category = categoryService.getCategoryByName(categoryName);
        } catch (EntityNotFoundException e) {
            // Create a new category if not found
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

    // Consumes messages from the "product-availability" Kafka topic to update product availability
    @Transactional
    @Incoming("product-availability")
    public void updateAvailabilityOfProduct(String payload) {
        try {
            // Deserialize the JSON payload into a ProductAvailabilityEvent object
            ProductAvailabilityEvent event = objectMapper.readValue(payload, ProductAvailabilityEvent.class);
            UUID productId = UUID.fromString(event.getProductId());
            Product product = productService.getProductDetails(productId);

            if (product != null) {
                // Update the product's availability status
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
