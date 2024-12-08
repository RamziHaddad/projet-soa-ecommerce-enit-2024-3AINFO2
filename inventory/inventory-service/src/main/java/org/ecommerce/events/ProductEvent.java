package org.ecommerce.events;
import java.util.UUID;

public class ProductEvent {
    private UUID productId;
    private int totalQuantity;
    private int reservedQuantity;
    private String eventType; 

    public ProductEvent(UUID productId, int totalQuantity, int reservedQuantity, String eventType) {
        this.productId = productId;
        this.totalQuantity = totalQuantity;
        this.reservedQuantity = reservedQuantity;
        this.eventType = eventType;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
