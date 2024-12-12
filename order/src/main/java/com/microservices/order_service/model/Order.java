package com.microservices.order_service.model;


import com.microservices.order_service.domain.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name = "idCart")
    private UUID idCart;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // Specify cascade and fetch type
    @JoinColumn(name = "id") // Foreign key in the Item table
    private List<Item> items; // Use List instead of Array


    @Column(name = "orderStatus")
    private OrderStatus orderStatus;  // Order status

    @Column(name = "price")
    private BigDecimal price;  // Price field

    @ManyToOne
    @JoinColumn(name = "idClient", referencedColumnName = "idClient")
    private Client client;  // Reference to the Client entity

    @Column(name = "quantity")
    private Integer quantity;  // Quantity field

    @Column(name = "orderNumber")
    private String orderNumber;  // Order number field

    @Column(name = "paymentVerification")
    private Boolean paymentVerification;  // Payment verification status

    @Column(name = "priceVerification")
    private Boolean priceVerification;  // Price verification status

    @Column(name = "deliveryVerification")
    private Boolean deliveryVerification;  // Delivery verification status

    @Column(name = "stockVerification")
    private Boolean stockVerification;  // Stock verification status

    @Column(name = "sentToShipmentAt")
    private LocalDateTime sentToShipmentAt;  // Shipment sent timestamp

    @Column(name = "receivedAt")
    private LocalDateTime receivedAt;  // Order received timestamp

    @Column(name = "coupon")
    private String coupon;  // Coupon code field
}
