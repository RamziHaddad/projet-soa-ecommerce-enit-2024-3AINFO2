package com.microservices.order_service.dto;
import com.microservices.order_service.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateRequestDTO {
    private UUID orderId;
    private OrderStatus status;
}
