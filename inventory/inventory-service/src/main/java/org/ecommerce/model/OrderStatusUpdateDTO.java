package org.ecommerce.model;

import java.util.UUID;

public class OrderStatusUpdateDTO {
    private UUID orderId;
    private String status;
    public OrderStatusUpdateDTO(){}

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String toString(){
        return "orderId: "+orderId+ ", status: "+status;
    }
}