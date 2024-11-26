package org.shipping.dto;

import java.util.UUID;

import org.shipping.model.Shipment.DeliveryStatus;

public class DeliveryStatusMessage {
    private UUID orderId;
    private DeliveryStatus status;

    public DeliveryStatusMessage () {}

    public DeliveryStatusMessage(UUID orderId, DeliveryStatus status) {
        this.orderId = orderId;
        this.status = status;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }
}
