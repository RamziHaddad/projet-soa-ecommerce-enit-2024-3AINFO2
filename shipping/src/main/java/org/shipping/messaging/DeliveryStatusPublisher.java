package org.shipping.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.shipping.dto.DeliveryStatusMessage;
import org.shipping.model.DeliveryStatus;

@ApplicationScoped
public class DeliveryStatusPublisher {

    @Inject
    @Channel("delivery-status")
    Emitter<DeliveryStatusMessage> statusEmitter;

    public void publishStatus(UUID orderId, DeliveryStatus status) {
        DeliveryStatusMessage message = new DeliveryStatusMessage(orderId, status);
        statusEmitter.send(message);
    }
}