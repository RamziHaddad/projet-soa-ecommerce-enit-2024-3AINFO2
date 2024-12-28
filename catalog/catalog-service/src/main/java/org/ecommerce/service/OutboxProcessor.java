package org.ecommerce.service;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.ecommerce.domain.OutboxEvent;
import org.ecommerce.domain.events.Event;
import org.ecommerce.domain.events.ProductListed;
import org.ecommerce.domain.events.ProductUpdated;
import java.util.concurrent.CompletionStage;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import io.quarkus.scheduler.Scheduled;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

// Service for processing outbox events and sending them to Kafka channels
// Handles scheduled processing of pending outbox messages
@ApplicationScoped
public class OutboxProcessor {

    private static final Logger logger = Logger.getLogger(OutboxProcessor.class);

    @Inject
    OutboxService outboxService;

    @Inject
    @Channel("products-out") // Kafka channel for ProductListed events
    Emitter<Event> productsListedEmitter;

    @Inject
    @Channel("products-updated") // Kafka channel for ProductUpdated events
    Emitter<Event> productsUpdatedEmitter;

    // Scheduled method that runs every 60 seconds to process outbox events
    @Scheduled(every = "60s")
    public void processOutbox() {
        List<OutboxEvent> messages = outboxService.getPendingMessages(); // Fetch pending messages

        for (OutboxEvent message : messages) {
            try {
                Event event = outboxService.convertToEvent(message); // Convert OutboxEvent to Event
                if (event instanceof ProductListed) {
                    sendProductListedEvent((ProductListed) event); // Send ProductListed event
                } else if (event instanceof ProductUpdated) {
                    sendProductUpdatedEvent((ProductUpdated) event); // Send ProductUpdated event
                }
            } catch (Exception e) {
                logger.error("Error processing outbox message with ID " + message.getId() + ": " + e.getMessage());
            }
        }
    }

    // Sends a ProductListed event to the Kafka channel
    private void sendProductListedEvent(ProductListed productListed) {
        sendEvent(productListed, productsListedEmitter);
    }

    // Sends a ProductUpdated event to the Kafka channel
    private void sendProductUpdatedEvent(ProductUpdated productUpdated) {
        sendEvent(productUpdated, productsUpdatedEmitter);
    }

    // Generic method to send events to a specified emitter
    private <T extends Event> void sendEvent(T event, Emitter<T> emitter) {
        try {
            CompletionStage<Void> ack = emitter.send(event); // Send event to Kafka
            ack.thenAccept(result -> {
                logger.info("Event sent via Kafka: " + event);
                outboxService.markAsSent(event.getEventId()); // Mark event as sent
            }).exceptionally(error -> {
                logger.error("Error when sending the event: " + error.getMessage());
                outboxService.markAsFailed(event.getEventId()); // Mark event as failed
                return null;
            });
        } catch (Exception e) {
            logger.error("Error when serializing JSON for event: " + e.getMessage());
            outboxService.markAsFailed(event.getEventId()); // Mark event as failed
        }
    }
}
