package org.ecommerce.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.ecommerce.model.Orders;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class OrderRepository {
    @Inject
    EntityManager em;

    @Transactional
    public Optional<Orders> getOrderByID(UUID id) {
        Orders order = em.find(Orders.class, id);
        return Optional.ofNullable(order);
    }

    @Transactional
    public Orders addOrder(Orders order) {
        em.persist(order);
        return order;
    }

    @Transactional
    public Orders updateOrder(Orders order) {
        return em.merge(order);
    }
}
