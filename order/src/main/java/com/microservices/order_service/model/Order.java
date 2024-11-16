package com.microservices.order_service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orderNumber")
    private String orderNumber;

    @Column(name = "skuCode")
    private String skuCode;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name="quantity")
    private Integer quantity;

}

