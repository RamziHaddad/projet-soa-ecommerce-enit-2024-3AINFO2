package payment.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import payment.domain.objectValues.PaymentStatus;

public class PaymentResponseDTO {
    private UUID paymentId;
    private UUID orderId;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private UUID customerId;

    public PaymentResponseDTO(UUID paymentId, UUID orderId, BigDecimal amount, PaymentStatus paymentStatus,
                              LocalDateTime paymentDate, UUID customerId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.customerId = customerId;
    }

    // Getters and Setters
    public UUID getPaymentId() {
        return paymentId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public UUID getCustomerId() {
        return customerId;
    }
}
