package com.microservices.order_service.repository;

import com.microservices.order_service.outbox.OrderEventOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderEventOutboxRepository extends JpaRepository<OrderEventOutbox, UUID> {
}
