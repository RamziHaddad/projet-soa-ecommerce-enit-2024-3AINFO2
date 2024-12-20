package payment.api.dto;

import java.util.UUID;

public class OrderUpdateRequestDTO {

      private UUID orderId;
     private String newStatus;

    public OrderUpdateRequestDTO() {}

    public OrderUpdateRequestDTO(UUID orderId, String newStatus) {
        this.orderId = orderId;
        this.newStatus = newStatus;
    }

    
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }
}

