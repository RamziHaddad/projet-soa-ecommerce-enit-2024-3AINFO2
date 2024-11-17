package com.microservices.order_service.dto;

public record ItemRequest(
        Long id,
        int quantity,
        double price
) {
}
