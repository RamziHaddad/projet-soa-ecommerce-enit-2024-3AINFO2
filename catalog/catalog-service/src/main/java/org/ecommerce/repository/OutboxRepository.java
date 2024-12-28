package org.ecommerce.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.ecommerce.domain.OutboxEvent;
import org.ecommerce.domain.events.Event;
import org.ecommerce.domain.events.ProductListed;
import org.ecommerce.domain.events.ProductUpdated;
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
            System.out.println("New OutboxEvent will be created in repo for " + event.getEventId().toString());
            String payload;

            if (event instanceof ProductListed productListed) {

                String productName = productListed.getProductName() != null ? productListed.getProductName() : "Unknown Product";
                String categoryName = productListed.getCategoryName() != null ? productListed.getCategoryName() : "Unknown Category";
                String description = productListed.getDescription() != null ? productListed.getDescription() : "No Description";
                BigDecimal price = productListed.getPrice().compareTo(BigDecimal.ZERO) > 0 ? productListed.getPrice() : BigDecimal.ZERO;

                payload = jsonMapper.writeValueAsString(Map.of(
                        "productName", productName,
                        "categoryName", categoryName,
                        "description", description,
                        "price", price
                ));
            } else if (event instanceof ProductUpdated productUpdated) {
                payload = jsonMapper.writeValueAsString(Map.of(
                        "productName", productUpdated.getProductName(),
                        "categoryName", productUpdated.getCategoryName(),
                        "description", productUpdated.getDescription(),
                        "shownPrice", productUpdated.getShownPrice(),
                        "disponibility", productUpdated.isDisponibility()
                ));
            } else {
                payload = jsonMapper.writeValueAsString(event);
                System.out.println("Not recognized case");
            }

            OutboxEvent oe = new OutboxEvent(event.getEventId(),
                    event.getEventType(),
                    "PENDING",
                    event.getCreatedAt(),
                    event.getAggregateType(),
                    event.getAggregateId(),
                    payload);
            System.out.println("New OutboxEvent created with ID: " + oe.getId());

            em.persist(oe);

            em.flush();

            System.out.println("OutboxEvent persisted successfully.");
            return oe;

        } catch (EntityExistsException e) {
            throw new EntityAlreadyExistsException("OutboxEvent entry already exists");
        } catch (JsonProcessingException e) {
            System.err.println("Failed to serialize event payload: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            throw e;
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
        em.createQuery("update OutboxEvent oe set oe.status = 'SENT' where oe.id=:oeid").setParameter("oeid", eventId).executeUpdate();
        
    }

    public void markAsFailed(UUID eventId) throws EntityNotFoundException {
        em.createQuery("update OutboxEvent oe set oe.status = 'FAILED' where oe.id=:oeid").setParameter("oeid", eventId).executeUpdate();
        
    }

    
}
