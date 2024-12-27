package com.microservices.order_service.dto;

import com.microservices.order_service.domain.OrderStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdateDTO {
    private UUID orderId;
    private OrderStatus status;
}
