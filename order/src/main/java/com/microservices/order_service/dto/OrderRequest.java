package com.microservices.order_service.dto;

import com.microservices.order_service.model.Item;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderRequest(
        Long id,         // Cart ID
        List<Item> items,            // List of items (nested record for items)
        String orderStatus                   // jj
) {
}

