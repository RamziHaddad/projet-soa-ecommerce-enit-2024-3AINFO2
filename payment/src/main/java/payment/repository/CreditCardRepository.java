package payment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import payment.domain.CreditCard;

public interface CreditCardRepository {
    CreditCard saveCreditCard(CreditCard creditCard);
    void deleteCreditCardById(UUID id);
    CreditCard updateCreditCard(CreditCard creditCard);
    Optional<CreditCard> findById(UUID id);  
    List<CreditCard> listCreditCards();
}
