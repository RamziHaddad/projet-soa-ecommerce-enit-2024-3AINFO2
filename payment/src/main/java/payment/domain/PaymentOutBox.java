package payment.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;

@Entity
public class PaymentOutBox {
    private UUID outBoxId ; 
    private UUID paymentUuid ; 
    private String payload; 
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;

    public UUID getOutBoxId() {
        return this.outBoxId;
    }

    public void setOutBoxId(UUID outBoxId) {
        this.outBoxId = outBoxId;
    }

    public UUID getPaymentUuid() {
        return this.paymentUuid;
    }

    public void setPaymentUuid(UUID paymentUuid) {
        this.paymentUuid = paymentUuid;
    }

    public String getPayload() {
        return this.payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getProcessedAt() {
        return this.processedAt;
    }
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
