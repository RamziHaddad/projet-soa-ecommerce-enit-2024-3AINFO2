package org.ecommerce.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.ecommerce.model.OrderEventDTO;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class OrderRepository {
    @Inject
    EntityManager em;

    @Transactional
    public Optional<OrderEventDTO> getOrderByID(UUID id) {
        OrderEventDTO order = em.find(OrderEventDTO.class, id);
        return Optional.ofNullable(order);
    }

    @Transactional
    public OrderEventDTO addOrder(OrderEventDTO order) {
        em.persist(order);
        return order;
    }

    @Transactional
    public OrderEventDTO updateOrder(OrderEventDTO order) {
        return em.merge(order);
    }
}
