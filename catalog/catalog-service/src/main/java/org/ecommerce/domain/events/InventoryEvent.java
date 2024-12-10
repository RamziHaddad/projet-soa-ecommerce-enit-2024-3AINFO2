package org.ecommerce.domain.events;


public class InventoryEvent {

    private String eventType;
    private String productId;
    private String name;
    private String description;
    private String category;
    private boolean disponibility;


    public String getEventType() {
        return eventType;
    }

    public String getProductId() {
        return productId;
    }
    
    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public boolean isDisponibility() {
        return disponibility;
    }

    public String getName() {
        return name;
    }
}
