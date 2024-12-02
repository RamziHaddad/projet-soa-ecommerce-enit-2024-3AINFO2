package org.ecommerce.model;

import java.util.List;
import java.util.UUID;

public class AvailabilityCheckDTO {
    private UUID orderId;

    private List<Item> items;


    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}

