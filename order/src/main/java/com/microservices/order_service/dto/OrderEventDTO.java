package com.microservices.order_service.dto;

import com.microservices.order_service.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderEventDTO {
    private UUID orderId;
    private List<Item> items;
    private String orderStatus;
}
