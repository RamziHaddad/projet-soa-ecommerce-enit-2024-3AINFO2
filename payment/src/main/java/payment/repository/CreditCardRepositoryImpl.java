package payment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import payment.domain.CreditCard;
@ApplicationScoped
@Transactional
public class CreditCardRepositoryImpl implements CreditCardRepository {

    @Inject
    EntityManager entityManager;

    @Override
    @Transactional
    public CreditCard saveCreditCard(CreditCard creditCard) {
        if (creditCard.getId() == null) {
            creditCard.setId(UUID.randomUUID());
        }
        entityManager.persist(creditCard);
        return creditCard;
    }

    @Override
    @Transactional
    public void deleteCreditCardById(UUID id) {
        Optional<CreditCard> creditCard = findById(id);
        creditCard.ifPresent(entityManager::remove);  // Remove if found
    }

    @Override
    @Transactional
    public CreditCard updateCreditCard(CreditCard creditCard) {
        return entityManager.merge(creditCard);
    }

    @Override
    public Optional<CreditCard> findById(UUID id) {
        CreditCard creditCard = entityManager.find(CreditCard.class, id);
        return Optional.ofNullable(creditCard);  
    }

    @Override
    public List<CreditCard> listCreditCards() {
        TypedQuery<CreditCard> query = entityManager.createQuery("SELECT c FROM CreditCard c", CreditCard.class);
        return query.getResultList();
    }
}