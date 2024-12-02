package org.ecommerce.events;
import java.util.UUID;
//The ProductEvent will be sent for the events that require knowing the hole product
public class ProductEvent extends  MinimalEvent {

    private int totalQuantity;
    private int reservedQuantity;

    public ProductEvent(UUID productId, int totalQuantity, int reservedQuantity, String eventType) {
        super(productId,eventType);

        this.totalQuantity = totalQuantity;
        this.reservedQuantity = reservedQuantity;

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


}
