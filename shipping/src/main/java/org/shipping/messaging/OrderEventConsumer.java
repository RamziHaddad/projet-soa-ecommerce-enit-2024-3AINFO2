package org.shipping.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.shipping.dto.OrderPaidEvent;
import org.shipping.service.ShippingService;

import jakarta.inject.Inject;

@ApplicationScoped
public class OrderEventConsumer {

    @Inject
    ShippingService shippingService;

    @Incoming("order-paid") // Correspond au topic configuré
    public void consumeOrderPaidEvent(OrderPaidEvent event) {
        // Créez une livraison à partir des données reçues
        shippingService.createShipment(event.getOrderId(), event.getAddressId());
    }
}
