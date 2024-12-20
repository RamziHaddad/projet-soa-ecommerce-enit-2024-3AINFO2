package org.ecommerce.domain.events;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductAvailabilityEvent extends Event {
    @JsonProperty("productId")
    private String productId;

    @JsonProperty("eventType")
    private final String availability;
    
    public ProductAvailabilityEvent(String productId, String availability) {
        super("ProductAvailability", "Product", productId);
        this.productId = productId;
        this.availability = availability;
    }
    
    @Override
    public String toString() {
        return "ProductAvailabilityEvent{" +
               ", eventType=" + getEventType() +
               ", aggregateType=" + getAggregateType() +
               ", aggregateId=" + getAggregateId() +
               '}';
    }

    public String getAvailability() {
        return availability;
    }

    public String getProductId() {
        return productId;
    }
    
}
