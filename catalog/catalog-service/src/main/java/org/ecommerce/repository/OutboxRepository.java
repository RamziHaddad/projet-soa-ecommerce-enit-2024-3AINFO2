package org.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.ecommerce.domain.OutboxEvent;
import org.ecommerce.domain.events.Event;
import org.ecommerce.exceptions.EntityNotFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.ecommerce.exceptions.EntityAlreadyExistsException;

@ApplicationScoped
public class OutboxRepository {

    @Inject
    EntityManager em;
    @Inject
    ObjectMapper jsonMapper;

    public List<OutboxEvent> findPendingMessages() {
        TypedQuery<OutboxEvent> query = em.createQuery(
                "SELECT o FROM OutboxEvent o WHERE o.status = 'PENDING'", OutboxEvent.class);
        return query.getResultList();
    }

    public OutboxEvent findById(UUID id) throws EntityNotFoundException {
        OutboxEvent outboxEvent = em.find(OutboxEvent.class, id);
        if (outboxEvent != null) {
            return outboxEvent;
        }
        throw new EntityNotFoundException("Cannot find outboxEvent entry with ID: " + id);
    }

    @Transactional
    public OutboxEvent insert(Event event) throws EntityAlreadyExistsException, JsonProcessingException {
        try {
            OutboxEvent oe=new OutboxEvent(event.getEventId(),
                                            event.getEventType(),
                                            "PENDING",
                                            event.getCreatedAt(),
                                            event.getAggregateType(),
                                            event.getAggregateId(),
                                            jsonMapper.writeValueAsString(event));
            em.persist(oe);
            return oe;
        } catch (EntityExistsException e) {
            throw new EntityAlreadyExistsException("OutboxEvent entry already exists");
        } catch (JsonProcessingException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public OutboxEvent update(OutboxEvent outboxEvent) throws EntityNotFoundException {
        try {
            return em.merge(outboxEvent);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Cannot find outboxEvent entry with ID: " + outboxEvent.getId());
        }
    }

    @Transactional
    public void delete(UUID id) {
        OutboxEvent outboxEvent = em.find(OutboxEvent.class, id);
        if (outboxEvent != null) {
            em.remove(outboxEvent);
        }
    }

    public void markAsSent(UUID eventId) throws EntityNotFoundException {
        em.createQuery("update OutboxEvent oe set oe.status = 'SENT' where oe.eventId=:oeid").setParameter("oeid", eventId).executeUpdate();
        throw new UnsupportedOperationException("Unimplemented method 'markAsSent'");
    }

    public void markAsFailed(UUID eventId) throws EntityNotFoundException {
        em.createQuery("update OutboxEvent oe set oe.status = 'FAILED' where oe.eventId=:oeid").setParameter("oeid", eventId).executeUpdate();
        throw new UnsupportedOperationException("Unimplemented method 'markAsFailed'");
    }
}
