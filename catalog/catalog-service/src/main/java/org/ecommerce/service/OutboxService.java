package org.ecommerce.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.ecommerce.domain.OutboxEvent;
import org.ecommerce.domain.events.Event;
import org.ecommerce.domain.events.ProductListed;
import org.ecommerce.domain.events.ProductUpdated;
import org.ecommerce.repository.OutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// Service for managing outbox events for eventual consistency in event-driven architecture.
// Handles creating, marking, and converting outbox messages.
@ApplicationScoped
public class OutboxService {
    private final Logger logger = LoggerFactory.getLogger(OutboxService.class);

    @Inject
    OutboxRepository outboxRepository;

    // Creates a new outbox message and saves it to the repository
    @Transactional
    public void createOutboxMessage(Event event) throws JsonProcessingException, EntityAlreadyExistsException {
        try {
            // Insert the event into the outbox repository
            outboxRepository.insert(event);
            System.out.println("Event inserted in outbox repo");
        } catch (EntityAlreadyExistsException e) {
            // Log and handle case when the event already exists
            logger.error("OutboxEvent already exists: " + e.getMessage());
            throw new RuntimeException("Failed to insert outbox event: Duplicate entry");
        } catch (JsonProcessingException e) {
            // Log and rethrow JSON processing exceptions
            logger.error("Failed to serialize event to JSON: " + e.getMessage());
            throw e;
        }
    }

    // Marks an outbox message as sent
    @Transactional
    public void markAsSent(UUID id) {
        try {
            // Mark the message as sent in the repository
            outboxRepository.markAsSent(id);
        } catch (EntityNotFoundException e) {
            // Log error if the message was not found
            System.err.println(e.getMessage());
        }
    }

    // Marks an outbox message as failed
    @Transactional
    public void markAsFailed(UUID id) {
        try {
            // Mark the message as failed in the repository
            outboxRepository.markAsFailed(id);
        } catch (EntityNotFoundException e) {
            // Log error if the message was not found
            System.err.println(e.getMessage());
        }
    }

    // Retrieves a list of pending outbox messages
    @Transactional
    public List<OutboxEvent> getPendingMessages() {
        return outboxRepository.findPendingMessages();
    }

    // Converts an OutboxEvent to its corresponding Event type
    @Transactional
    public Event convertToEvent(OutboxEvent outboxEvent) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // Convert the message from JSON to a map
            @SuppressWarnings("unchecked")
            Map<String, Object> messageMap = objectMapper.readValue(outboxEvent.getMessage(), Map.class);

            System.out.println("Message Map: " + messageMap);

            // Log each entry in the message map for debugging
            for (Map.Entry<String, Object> entry : messageMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                System.out.println("Key: " + key + ", Value: " + value + ", Type: " + (value != null ? value.getClass().getName() : "null"));
            }

            String eventType = outboxEvent.getEventType();
            Event event = null;

            // Handle conversion for ProductListed event type
            if ("ProductListed".equals(eventType)) {
                ProductListed productListed = new ProductListed();
                productListed.setProductName(objectMapper.convertValue(messageMap.get("productName"), String.class));
                productListed.setCategoryName(objectMapper.convertValue(messageMap.get("categoryName"), String.class));
                productListed.setDescription(objectMapper.convertValue(messageMap.get("description"), String.class));
                productListed.setPrice(objectMapper.convertValue(messageMap.get("price"), BigDecimal.class));

                // Set common properties for the event
                productListed.setEventId(outboxEvent.getId());
                productListed.setEventType(eventType);
                productListed.setAggregateId(outboxEvent.getAggregateId());
                productListed.setCreatedAt(outboxEvent.getCreatedAt());
                productListed.setAggregateType(outboxEvent.getAggregateType());

                event = productListed;
            }
            // Handle conversion for ProductUpdated event type
            else if ("ProductUpdated".equals(eventType)) {
                ProductUpdated productUpdated = new ProductUpdated();
                productUpdated.setProductName(objectMapper.convertValue(messageMap.get("productName"), String.class));
                productUpdated.setDescription(objectMapper.convertValue(messageMap.get("description"), String.class));
                productUpdated.setCategoryName(objectMapper.convertValue(messageMap.get("categoryName"), String.class));
                productUpdated.setShownPrice(objectMapper.convertValue(messageMap.get("shownPrice"), BigDecimal.class));
                productUpdated.setDisponibility(objectMapper.convertValue(messageMap.get("disponibility"), Boolean.class));

                // Set common properties for the event
                productUpdated.setEventId(outboxEvent.getId());
                productUpdated.setEventType(eventType);
                productUpdated.setAggregateId(outboxEvent.getAggregateId());
                productUpdated.setCreatedAt(outboxEvent.getCreatedAt());
                productUpdated.setAggregateType(outboxEvent.getAggregateType());

                event = productUpdated;
            }

            // If the event type is unknown, throw an exception
            if (event == null) {
                throw new IllegalArgumentException("Unknown event type: " + eventType);
            }

            return event;
        } catch (JsonProcessingException e) {
            // Throw an exception if JSON processing fails
            throw new Exception("Failed to convert OutboxEvent to Event: " + e.getMessage(), e);
        }
    }
}
