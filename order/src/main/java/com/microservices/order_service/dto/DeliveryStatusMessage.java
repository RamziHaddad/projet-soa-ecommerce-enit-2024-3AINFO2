package com.microservices.order_service.dto;

import com.microservices.order_service.domain.DeliveryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryStatusMessage {
    private UUID orderId;
    private DeliveryStatus status;
    private UUID cartId;
}
