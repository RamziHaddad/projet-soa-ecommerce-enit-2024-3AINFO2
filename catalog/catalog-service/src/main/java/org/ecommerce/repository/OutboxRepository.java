package org.ecommerce.repository;

import java.util.List;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import org.ecommerce.domain.Outbox;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.exceptions.EntityAlreadyExistsException;

@ApplicationScoped
public class OutboxRepository {

    @Inject
    EntityManager em;

    public List<Outbox> findAll() {
        return em.createQuery("from Outbox", Outbox.class)
                .getResultList();
    }

    public List<Outbox> findPendingMessages() {
        TypedQuery<Outbox> query = em.createQuery(
                "SELECT o FROM Outbox o WHERE o.status = 'PENDING'", Outbox.class);
        return query.getResultList();
    }

    public Outbox findById(UUID id) throws EntityNotFoundException {
        Outbox outbox = em.find(Outbox.class, id);
        if (outbox != null) {
            return outbox;
        }
        throw new EntityNotFoundException("Cannot find outbox entry with ID: " + id);
    }

    @Transactional
    public Outbox insert(Outbox outbox) throws EntityAlreadyExistsException {
        try {
            outbox.setId(UUID.randomUUID());
            em.persist(outbox);
            return outbox;
        } catch (EntityExistsException e) {
            throw new EntityAlreadyExistsException("Outbox entry already exists");
        }
    }

    @Transactional
    public Outbox update(Outbox outbox) throws EntityNotFoundException {
        try {
            return em.merge(outbox);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException("Cannot find outbox entry with ID: " + outbox.getId());
        }
    }

    @Transactional
    public void delete(UUID id) {
        Outbox outbox = em.find(Outbox.class, id);
        if (outbox != null) {
            em.remove(outbox);
        }
    }
}
