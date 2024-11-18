package payment.domain;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class CreditCard {
    @Id
    private UUID id ; 
    private Long cardCode ;  
    private UUID customerId ;
    private int secretNumber ;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getCardCode() {
        return this.cardCode;
    }

    public void setCardCode(Long cardCode) {
        this.cardCode = cardCode;
    }

    public UUID getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public int getSecretNumber() {
        return this.secretNumber;
    }

    public void setSecretNumber(int secretNumber) {
        this.secretNumber = secretNumber;
    }

}
