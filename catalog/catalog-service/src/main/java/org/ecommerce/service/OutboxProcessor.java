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


@ApplicationScoped
public class OutboxProcessor {

    private static final Logger logger = Logger.getLogger(OutboxProcessor.class);

    @Inject
    OutboxService outboxService;

    @Inject
    @Channel("products-out")
    Emitter<Event> productsListedEmitter;

    @Inject
    @Channel("products-updated")
    Emitter<Event> productsUpdatedEmitter;

    @Scheduled(every = "60s")
    public void processOutbox() {
        List<OutboxEvent> messages = outboxService.getPendingMessages();

        for (OutboxEvent message : messages) {
            try {
                Event event = outboxService.convertToEvent(message);
                if (event instanceof ProductListed) {
                    sendProductListedEvent((ProductListed) event);
                } else if (event instanceof ProductUpdated) {
                    sendProductUpdatedEvent((ProductUpdated) event);
                }
            } catch (Exception e) {
                logger.error("Error processing outbox message with ID " + message.getId() + ": " + e.getMessage());
            }
        }
    }

    private void sendProductListedEvent(ProductListed productListed) {
        try {
            CompletionStage<Void> ack = productsListedEmitter.send(productListed);
            ack.thenAccept(result -> {
                logger.info("Product listed and sent via Kafka: " + productListed);
                outboxService.markAsSent(productListed.getEventId());
            }).exceptionally(error -> {
                logger.error("Error when sending the product listed event: " + error.getMessage());
                outboxService.markAsFailed(productListed.getEventId());
                return null;
            });
        } catch (Exception e) {
            logger.error("Error when serializing JSON for ProductListed event: " + e.getMessage());
            outboxService.markAsFailed(productListed.getEventId());
        }
    }

    private void sendProductUpdatedEvent(ProductUpdated productUpdated) {
        try {
            CompletionStage<Void> ack = productsUpdatedEmitter.send(productUpdated);
            ack.thenAccept(result -> {
                logger.info("Product updated and sent via Kafka: " + productUpdated);
                outboxService.markAsSent(productUpdated.getEventId());
            }).exceptionally(error -> {
                logger.error("Error when sending the product updated event: " + error.getMessage());
                outboxService.markAsFailed(productUpdated.getEventId());
                return null;
            });
        } catch (Exception e) {
            logger.error("Error when serializing JSON for ProductUpdated event: " + e.getMessage());
            outboxService.markAsFailed(productUpdated.getEventId());
        }
    }
}
