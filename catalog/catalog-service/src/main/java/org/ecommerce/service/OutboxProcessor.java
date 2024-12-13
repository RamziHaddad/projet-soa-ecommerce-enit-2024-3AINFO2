package org.ecommerce.service;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.ecommerce.domain.OutboxEvent;
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
    Emitter<String> productsEmitter;

    @Scheduled(every = "60s")
    public void processOutbox() {
        List<OutboxEvent> messages = outboxService.getPendingMessages();

        for (OutboxEvent message : messages) {
            try {
                productsEmitter.send(message.getMessage()).thenRun(() -> {
                    outboxService.markAsSent(message.getId());
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
