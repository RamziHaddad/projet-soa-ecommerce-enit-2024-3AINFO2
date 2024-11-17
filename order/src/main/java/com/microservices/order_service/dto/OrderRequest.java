package com.microservices.order_service.dto;

import com.microservices.order_service.model.Item;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderRequest(
        Long idCart,                        // Cart ID
        List<Item> items,            // List of items (nested record for items)
        String orderStatus,                 // Order status
        BigDecimal price,                   // Total price
        Long idClient,                      // Client ID
        Integer quantity,                   // Total quantity
        String orderNumber,                 // Order number
        Boolean paymentVerification,        // Payment verification status
        Boolean priceVerification,          // Price verification status
        Boolean deliveryVerification,       // Delivery verification status
        Boolean stockVerification,          // Stock verification status
        LocalDateTime sentToShipmentAt,     // Shipment sent timestamp
        LocalDateTime receivedAt,           // Order received timestamp
        String coupon                       // Coupon code
) {
}

