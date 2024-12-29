package com.microservices.order_service.repository;

import com.microservices.order_service.outbox.OrderPaidOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderPaidOutboxRepository extends JpaRepository<OrderPaidOutbox, UUID> {
}
