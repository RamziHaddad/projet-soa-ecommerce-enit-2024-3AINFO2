package org.ecommerce.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.ecommerce.domain.OutboxEvent;
import org.ecommerce.domain.events.Event;
import org.ecommerce.repository.OutboxRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OutboxService {

    @Inject
    OutboxRepository outboxRepository;

    @Transactional
    public void createOutboxMessage(Event event) throws JsonProcessingException, EntityAlreadyExistsException {
        try {
            System.out.println("Creating outbox message in service "+event.getEventId().toString());
            outboxRepository.insert(event);
        } catch (EntityAlreadyExistsException e) {
            //logger.error("OutboxEvent already exists: " + e.getMessage());
            throw new RuntimeException("Failed to insert outbox event: Duplicate entry");
        } catch (JsonProcessingException e) {
            //logger.error("Failed to serialize event to JSON: " + e.getMessage());
            throw e; 
        } catch (Exception e) {
            //logger.error("Unexpected error during outbox message creation: " + e.getMessage());
            throw new RuntimeException("Unexpected error during outbox message creation", e);
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

    public Event convertToEvent(OutboxEvent outboxEvent) throws Exception {
        try {
            return new ObjectMapper().readValue(outboxEvent.getMessage(), Event.class);
        } catch (JsonProcessingException e) {
            //logger.error("Failed to convert OutboxEvent to Event: " + e.getMessage());
            throw e;
        }
    }
}
