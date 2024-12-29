package com.microservices.order_service.events;

import com.microservices.order_service.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaidEvent implements Serializable {
    @Setter
    private UUID orderId;
    @Setter
    private UUID addressId;
    @Setter
    private UUID cartId;
    private LocalDateTime timestamp;
    @Setter
    private PaymentStatus paymentStatus;





    @Override
    public String toString() {
        return "OrderPaidEvent{" +
                "orderId=" + orderId +
                ", addressId=" + addressId +
                ", cartId=" + cartId +
                ", timestamp=" + timestamp +
                ", paymentStatus=" + paymentStatus +
                '}';
    }
}