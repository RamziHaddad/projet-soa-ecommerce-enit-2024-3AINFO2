package org.ecommerce.service;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.ecommerce.domain.OutboxEvent;
import org.ecommerce.domain.events.Event;
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
    Emitter<Event> productsEmitter;

    @Scheduled(every = "20s")
    public void processOutbox() {
        List<OutboxEvent> messages = outboxService.getPendingMessages();

        for (OutboxEvent message : messages) {
            try {
                Event event = outboxService.convertToEvent(message);
                
                 productsEmitter.send(event).thenRun(() -> {
                     outboxService.markAsSent(event.getEventId());
                     logger.info("Successfully sent outbox message with ID: " + message.getId());
                 }).exceptionally(e -> {
                     logger.error("Failed to send outbox message with ID " + message.getId() + ": " + e.getMessage());
                     outboxService.markAsFailed(message.getId());
                     return null;
                 });
            } catch (Exception e) {
                logger.error("Error processing outbox message with ID " + message.getId() + ": " + e.getMessage());
            }
        }
    }
}
