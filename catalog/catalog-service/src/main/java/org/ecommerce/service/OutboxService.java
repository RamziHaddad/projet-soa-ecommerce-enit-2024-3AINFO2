package org.ecommerce.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.ecommerce.domain.Outbox;
import org.ecommerce.repository.OutboxRepository;
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
    public Outbox createOutboxMessage(String message) {
        Outbox outbox = new Outbox();
        outbox.setMessage(message);
        outbox.setStatus("PENDING");
        outbox.setCreatedAt(Instant.now().toString());
        try {
            return outboxRepository.insert(outbox);
        } catch (EntityAlreadyExistsException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Transactional
    public void markAsSent(UUID id) {
        try {
            Outbox outbox = outboxRepository.findById(id);
            outbox.setStatus("SENT");
            outbox.setSentAt(Instant.now().toString());
            outboxRepository.update(outbox);
        } catch (EntityNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    @Transactional
    public void markAsFailed(UUID id) {
        try {
            Outbox outbox = outboxRepository.findById(id);
            outbox.setStatus("FAILED");
            outboxRepository.update(outbox);
        } catch (EntityNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    @Transactional
    public List<Outbox> getPendingMessages() {
        return outboxRepository.findPendingMessages();
    }
}
