package com.microservices.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestWrapperDTO {
    private List<CartItem> cartItems;
    private PaymentRequestDTO paymentRequestDTO;

    // Getters and Setters
}

