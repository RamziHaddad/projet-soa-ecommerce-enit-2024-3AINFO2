package payment.api.dto;

import java.util.UUID;

public class CreditCardRequestDTO {
    private UUID id;
    private Long cardCode;
    private UUID customerId;
    private int secretNumber;

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

    public int getSecretNumber() {
        return secretNumber;
    }

    public void setSecretNumber(int secretNumber) {
        this.secretNumber = secretNumber;
    }
}
