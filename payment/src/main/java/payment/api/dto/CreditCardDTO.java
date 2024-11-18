package payment.api.dto;

import java.util.UUID;

public class CreditCardDTO {

    private UUID id;
    private Long cardCode; // Masked or partial card number can be used
    private UUID customerId;

    // Constructors
    public CreditCardDTO() {}

    public CreditCardDTO(UUID id, Long cardCode, UUID customerId) {
        this.id = id;
        this.cardCode = cardCode;
        this.customerId = customerId;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getCardCode() {
        return cardCode;
    }

    public void setCardCode(Long cardCode) {
        this.cardCode = cardCode;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }
}
