package com.microservices.order_service.outbox;


import com.microservices.order_service.domain.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaidOutbox {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID addressId;

    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    @Column(nullable = false)
    private UUID cartId;

    @Column
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Boolean processed;
}
