package com.microservices.order_service.dto;

import com.microservices.order_service.domain.PaymentStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public record PaymentResponse(
        UUID paymentId,
        UUID orderId,
        BigDecimal amount,
        PaymentStatus paymentStatus,
        LocalDateTime paymentDate,
        UUID customerId
) {
}

