package com.entities;

import java.util.Date;

public class ShippingOrder {
    private Long orderId;
    private Date orderDate;
    private ShippingAddress shippingAddress;
    private OrderStatus status;

    // Getters and Setters

    public enum OrderStatus {
        PENDING, SHIPPED, DELIVERED, CANCELLED
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public ShippingAddress getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    
}
