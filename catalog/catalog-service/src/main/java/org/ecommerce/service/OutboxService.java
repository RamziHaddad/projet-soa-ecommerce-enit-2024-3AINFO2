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

@ApplicationScoped
public class OutboxService {
    private final Logger logger = LoggerFactory.getLogger(OutboxService.class);

    @Inject
    OutboxRepository outboxRepository;

    @Transactional
    public void createOutboxMessage(Event event) throws JsonProcessingException, EntityAlreadyExistsException {
        try {
            System.out.println("Creating outbox message in service "+event.getEventId().toString());
            outboxRepository.insert(event);
        } catch (EntityAlreadyExistsException e) {
            logger.error("OutboxEvent already exists: " + e.getMessage());
            throw new RuntimeException("Failed to insert outbox event: Duplicate entry");
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize event to JSON: " + e.getMessage());
            throw e; 
        }
    }

    @Transactional
    public void markAsSent(UUID id) {
        try {
            //outbox.setSentAt(Instant.now().toString());
            outboxRepository.markAsSent(id);
        } catch (EntityNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    @Transactional
    public void markAsFailed(UUID id) {
        try {
            outboxRepository.markAsFailed(id);
        } catch (EntityNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    @Transactional
    public List<OutboxEvent> getPendingMessages() {
        return outboxRepository.findPendingMessages();
    }

    @Transactional
public Event convertToEvent(OutboxEvent outboxEvent) throws Exception {
    try {
        ObjectMapper objectMapper = new ObjectMapper();
        @SuppressWarnings("unchecked")
        Map<String, Object> messageMap = objectMapper.readValue(outboxEvent.getMessage(), Map.class);


        // Output all types of the message map for debugging
        System.out.println("Message Map: " + messageMap);
        
        // Print each key, value, and the type of the value in the messageMap
        for (Map.Entry<String, Object> entry : messageMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + value + ", Type: " + (value != null ? value.getClass().getName() : "null"));
        }
        String eventType = outboxEvent.getEventType();
        Event event = null;

        if ("ProductListed".equals(eventType)) {
            ProductListed productListed = new ProductListed();
            productListed.setProductName(objectMapper.convertValue(messageMap.get("productName"), String.class));
            productListed.setCategoryName(objectMapper.convertValue(messageMap.get("categoryName"), String.class));
            productListed.setDescription(objectMapper.convertValue(messageMap.get("description"), String.class));
            productListed.setPrice(objectMapper.convertValue(messageMap.get("price"), BigDecimal.class));

            productListed.setEventId(outboxEvent.getId());
            productListed.setEventType(eventType);
            productListed.setAggregateId(outboxEvent.getAggregateId());
            productListed.setCreatedAt(outboxEvent.getCreatedAt());
            productListed.setAggregateType(outboxEvent.getAggregateType());

            event = productListed;
        } else if ("ProductUpdated".equals(eventType)) {
            ProductUpdated productUpdated = new ProductUpdated();
            productUpdated.setProductName(objectMapper.convertValue(messageMap.get("productName"), String.class));
            productUpdated.setDescription(objectMapper.convertValue(messageMap.get("description"), String.class));
            productUpdated.setCategoryName(objectMapper.convertValue(messageMap.get("categoryName"), String.class));
            productUpdated.setShownPrice(objectMapper.convertValue(messageMap.get("shownPrice"), BigDecimal.class));
            productUpdated.setDisponibility(objectMapper.convertValue(messageMap.get("disponibility"), Boolean.class));

            productUpdated.setEventId(outboxEvent.getId());
            productUpdated.setEventType(eventType);
            productUpdated.setAggregateId(outboxEvent.getAggregateId());
            productUpdated.setCreatedAt(outboxEvent.getCreatedAt());
            productUpdated.setAggregateType(outboxEvent.getAggregateType());

            event = productUpdated;
        }

        if (event == null) {
            throw new IllegalArgumentException("Unknown event type: " + eventType);
        }

        return event;
    } catch (JsonProcessingException e) {
        throw new Exception("Failed to convert OutboxEvent to Event: " + e.getMessage(), e);
    }
}

}
