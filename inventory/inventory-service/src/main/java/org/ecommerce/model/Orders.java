package org.ecommerce.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Orders {
    @Id
    private UUID orderId;
    @OneToMany
    @JoinColumn(name="orderId")
    private List<Item> items;
    private String status;

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
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
