package org.ecommerce.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.ecommerce.model.Order;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class OrderRepository {
    @Inject
    EntityManager em;

    @Transactional
    public Optional<Order> getOrderByID(UUID id) {
        Order order = em.find(Order.class, id);
        return Optional.ofNullable(order);
    }

    @Transactional
    public Order addOrder(Order order) {
        em.persist(order);
        return order;
    }

    @Transactional
    public Order updateOrder(Order order) {
        return em.merge(order);
    }
}
