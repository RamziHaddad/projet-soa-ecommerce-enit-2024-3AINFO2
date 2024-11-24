package payment.api.dto;

import java.util.UUID;

public class CreditCardRequestDTO {
    private UUID id;
    private int cardCode;
    private UUID customerId;
    private int secretNumber;

    public CreditCardRequestDTO() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public int getCardCode() {
        return cardCode;
    }

    public void setCardCode(int cardCode) {
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
