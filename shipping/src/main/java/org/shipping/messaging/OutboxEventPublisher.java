package org.shipping.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.shipping.config.EventMappingConfig;
import org.shipping.model.OutboxEvent;
import org.shipping.model.DeliveryStatus;
import org.shipping.repository.OutboxRepository;

import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OutboxEventPublisher {

    private static final Logger logger = Logger.getLogger(OutboxEventPublisher.class);

    @Inject
    OutboxRepository outboxRepository;

    @Inject
    DeliveryStatusPublisher deliveryStatusPublisher;

    @Inject
    EventMappingConfig eventMappingConfig;

    @Scheduled(every = "60s")
    public void processOutbox() {
        // Récupération des événements non traités
        List<OutboxEvent> events = outboxRepository.find("processed = 'false'").list();

        for (OutboxEvent event : events) {
            try {
                // Extraction du type d'événement
                String eventType = event.getEventType();
                String statusName = eventMappingConfig.getDeliveryStatus(eventType);

                if (statusName != null) {
                    // Conversion du statut et publication
                    DeliveryStatus status = DeliveryStatus.valueOf(statusName);
                    deliveryStatusPublisher.publishStatus(UUID.fromString(event.getPayload()), status);

                    // Journaux pour le débogage
                    logger.infof("Traitement de l'événement %s avec le type %s", event.getId(), event.getEventType());
                    logger.infof("Statut publié : %s", statusName);

                    // Marquage de l'événement comme traité
                    event.setProcessed("true");
                    outboxRepository.persist(event);
                    logger.info("L'événement a été marqué comme traité.");
                } else {
                    // Aucun mappage trouvé
                    logger.warnf("Aucun mappage trouvé pour le type d'événement : %s", eventType);
                }
            } catch (Exception e) {
                // Gestion des erreurs
                logger.errorf("Erreur lors du traitement de l'événement %s : %s", event.getId(), e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
