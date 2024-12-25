package org.shipping.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import org.shipping.model.OutboxEvent;

@ApplicationScoped
public class OutboxRepository implements PanacheRepository<OutboxEvent> {
}
