package com.microservices.order_service.dto;

import java.util.UUID;

public record ItemRequest(
        UUID  ProductId,
        int quantity
) {
}
