package org.ecommerce.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.ecommerce.domain.OutboxEvent;
import org.ecommerce.domain.events.Event;
import org.ecommerce.repository.OutboxRepository;

import com.fasterxml.jackson.core.JsonProcessingException;

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
        outboxRepository.insert(event);
        try {
            outboxRepository.insert(event);
        } catch (EntityAlreadyExistsException e) {
            System.err.println(e.getMessage());
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
}
