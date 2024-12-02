package org.ecommerce.events;

import java.util.UUID;

//The MinimalEvent will be sent for the events that require knowing only the id product
public class MinimalEvent {
    private UUID eventID;
    private UUID productId;
    private String eventType;


    public MinimalEvent(UUID productId,String eventType){

        this.productId=productId;
        this.eventType=eventType;
    }

    public UUID getEventID() {
        return eventID;
    }

    public void setEventID(UUID eventID) {
        this.eventID = eventID;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
