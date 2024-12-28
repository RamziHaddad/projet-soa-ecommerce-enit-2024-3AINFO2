package org.ecommerce.domain.events;

public class ProductAvailabilityEvent {
    private String productId;
    private String availability;
    
    public ProductAvailabilityEvent() {
    }
    public ProductAvailabilityEvent(String productId, String availability) {
        this.productId = productId;
        this.availability = availability;
    }
    

    public String getAvailability() {
        return availability;
    }

    public String getProductId() {
        return productId;
    }
    
}
