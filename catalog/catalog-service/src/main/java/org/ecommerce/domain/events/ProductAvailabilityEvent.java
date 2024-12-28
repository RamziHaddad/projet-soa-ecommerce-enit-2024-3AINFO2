package org.ecommerce.domain.events;

// Class representing the availability status of a product
public class ProductAvailabilityEvent {
    private String productId; // Unique identifier for the product
    private String availability; // Availability status of the product (e.g., "In Stock", "Out of Stock")

    // Default constructor
    public ProductAvailabilityEvent() {
    }

    // Constructor to initialize productId and availability
    public ProductAvailabilityEvent(String productId, String availability) {
        this.productId = productId;
        this.availability = availability;
    }

    // Getter for availability
    public String getAvailability() {
        return availability;
    }

    // Getter for productId
    public String getProductId() {
        return productId;
    }
}
