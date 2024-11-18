package payment.services.mappers;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import payment.api.dto.CreditCardRequestDTO;
import payment.api.dto.CreditCardResponseDTO;
import payment.domain.CreditCard;

@ApplicationScoped
public class CreditCardMapper {

    
    public CreditCard toEntity(CreditCardRequestDTO dto) {
        CreditCard creditCard = new CreditCard();
        creditCard.setId(dto.getId() != null ? dto.getId() : UUID.randomUUID()); 
        creditCard.setCardCode(dto.getCardCode());
        creditCard.setCustomerId(dto.getCustomerId());
        creditCard.setSecretNumber(dto.getSecretNumber());
        return creditCard;
    }

    
    public CreditCardResponseDTO toResponseDTO(CreditCard creditCard) {
        return new CreditCardResponseDTO(
                creditCard.getId(),
                creditCard.getCardCode(),
                creditCard.getCustomerId(),
                creditCard.getSecretNumber()
        );
    }
}
