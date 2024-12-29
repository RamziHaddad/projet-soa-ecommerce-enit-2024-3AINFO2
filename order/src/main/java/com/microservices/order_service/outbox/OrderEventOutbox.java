package com.microservices.order_service.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventOutbox {

    @Id
    @GeneratedValue
    private UUID id;


    @Column(nullable = false)
    private UUID orderId;


    @Size(max = 10000)
    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private Boolean processed;



}
