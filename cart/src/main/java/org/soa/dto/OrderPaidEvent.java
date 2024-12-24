package org.soa.dto;

import java.util.UUID;

public class OrderPaidEvent {
    private UUID orderId;
    private UUID cartId;
    private UUID addressId;

    // Constructeurs, getters et setters
    public OrderPaidEvent() {
    }

    public UUID getCartId() {
        return cartId;
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