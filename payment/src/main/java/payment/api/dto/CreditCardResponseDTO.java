package payment.api.dto;

import java.util.UUID;

public class CreditCardResponseDTO {
    private UUID id;
    private int cardCode;
    private UUID customerId;
    private int secretNumber;

    // Constructor
    public CreditCardResponseDTO(UUID id, int cardCode, UUID customerId, int secretNumber) {
        this.id = id;
        this.cardCode = cardCode;
        this.customerId = customerId;
        this.secretNumber = secretNumber;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public int getCardCode() {
        return cardCode;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public int getSecretNumber() {
        return secretNumber;
    }
}
