package org.shipping.dto;

import java.util.UUID;

public class OrderPaidEvent {
    private UUID orderId;
    private UUID addressId;

    // Constructeurs, getters et setters
    public OrderPaidEvent() {
    }

    public OrderPaidEvent(UUID orderId, UUID addressId) {
        this.orderId = orderId;
        this.addressId = addressId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getAddressId() {
        return addressId;
    }
}
