package com.microservices.order_service.repository;

import com.microservices.order_service.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
