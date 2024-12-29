package com.microservices.order_service.repository;

import com.microservices.order_service.outbox.OrderStatusOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderStatusOutboxRepository extends JpaRepository<OrderStatusOutbox, UUID> {
}
