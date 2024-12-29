package org.soa.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.soa.dto.OrderPaidEvent;
import org.soa.service.CartService;

import jakarta.inject.Inject;

@ApplicationScoped
public class OrderEventConsumer {

    @Inject
    CartService cartService;

    @Incoming("order-paid") // Correspond au topic configuré
    public void consumeOrderPaidEvent(OrderPaidEvent event) {
        // Vider le panier
        cartService.clearCart(event.getCartId());
    }
}