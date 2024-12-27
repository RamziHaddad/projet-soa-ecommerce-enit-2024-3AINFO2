package com.microservices.order_service.dto;

import java.util.UUID;

public record OrderDeliveryDTO(UUID clientId, UUID orderId, String clientAddress) {

}
