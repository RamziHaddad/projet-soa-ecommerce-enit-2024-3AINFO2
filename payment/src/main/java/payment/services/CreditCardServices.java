package payment.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import payment.api.dto.CreditCardRequestDTO;
import payment.api.dto.CreditCardResponseDTO;

public interface CreditCardServices {
    
    CreditCardResponseDTO registerCreditCard(CreditCardRequestDTO creditCardRequest);

    
    void deleteCreditCard(UUID id);

    
    CreditCardResponseDTO updateCreditCard(CreditCardRequestDTO creditCardRequest);

    
    Optional<CreditCardResponseDTO> getCreditCardById(UUID id);
    Optional<CreditCardResponseDTO> getCreditCardByCustomerId(UUID customerId);

    
    List<CreditCardResponseDTO> getAllCreditCards();
}
