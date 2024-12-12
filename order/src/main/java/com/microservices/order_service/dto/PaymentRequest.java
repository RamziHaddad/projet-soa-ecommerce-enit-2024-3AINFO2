package com.microservices.order_service.dto;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;


public record PaymentRequest(
        UUID id,
        BigDecimal amount,
        UUID  idClient,         // Client ID
        BigInteger numero_cart_bancaire,
        BigInteger code_secret
) {
}

