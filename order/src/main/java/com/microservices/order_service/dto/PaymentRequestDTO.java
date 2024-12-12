package com.microservices.order_service.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private UUID id;
    private BigDecimal amount;
    private UUID  idClient;       // Client ID
    private BigInteger numero_cart_bancaire;
    private BigInteger code_secret;

}


