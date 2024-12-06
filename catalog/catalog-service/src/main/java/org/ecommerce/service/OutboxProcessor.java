package org.ecommerce.service;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.ecommerce.domain.Outbox;
import org.ecommerce.service.OutboxService;
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

    @Scheduled(every = "5s")
    public void processOutbox() {
        List<Outbox> messages = outboxService.getPendingMessages();

        for (Outbox message : messages) {
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
