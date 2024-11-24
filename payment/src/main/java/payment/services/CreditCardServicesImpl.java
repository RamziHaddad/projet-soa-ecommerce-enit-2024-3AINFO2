package payment.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import payment.api.dto.CreditCardRequestDTO;
import payment.api.dto.CreditCardResponseDTO;
import payment.domain.CreditCard;
import payment.repository.CreditCardRepository;
import payment.services.mappers.CreditCardMapper;

@ApplicationScoped
public class CreditCardServicesImpl implements CreditCardServices {

    @Inject
    CreditCardRepository creditCardRepository;

    @Inject
    CreditCardMapper creditCardMapper;

    @Override
    public CreditCardResponseDTO registerCreditCard(CreditCardRequestDTO creditCardRequest) {
        
        CreditCard creditCard = creditCardMapper.toEntity(creditCardRequest);

        
        CreditCard savedCreditCard = creditCardRepository.saveCreditCard(creditCard);

        
        return creditCardMapper.toResponseDTO(savedCreditCard);
    }

    @Override
    public void deleteCreditCard(UUID id) {
        creditCardRepository.deleteCreditCardById(id);
    }

    @Override
    public CreditCardResponseDTO updateCreditCard(CreditCardRequestDTO creditCardRequest) {
        
        CreditCard creditCard = creditCardMapper.toEntity(creditCardRequest);

        
        CreditCard updatedCreditCard = creditCardRepository.updateCreditCard(creditCard);

        
        return creditCardMapper.toResponseDTO(updatedCreditCard);
    }

    @Override
    public Optional<CreditCardResponseDTO> getCreditCardById(UUID id) {
        return creditCardRepository.findById(id)
                .map(creditCardMapper::toResponseDTO);
    }

    @Override
    public List<CreditCardResponseDTO> getAllCreditCards() {
        return creditCardRepository.listCreditCards()
                .stream()
                .map(creditCardMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CreditCardResponseDTO> getCreditCardByCustomerId(UUID customerId) {
        return creditCardRepository.findByCustomerId(customerId)
                .map(creditCardMapper::toResponseDTO);
    }
}
