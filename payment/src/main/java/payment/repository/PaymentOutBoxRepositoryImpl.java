package payment.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import payment.domain.PaymentOutBox;

import java.util.List;

@ApplicationScoped
@Transactional
public class PaymentOutBoxRepositoryImpl implements PaymentOutBoxRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public PaymentOutBox save(PaymentOutBox paymentOutBox) {
        em.persist(paymentOutBox); // Persist the entity into the database
        return paymentOutBox;
    }

    @Override
    public PaymentOutBox update(PaymentOutBox paymentOutBox) {
        return em.merge(paymentOutBox); // Update the entity in the database
    }

    @Override
    public PaymentOutBox findById(Long id) {
        return em.find(PaymentOutBox.class, id); // Find the entity by ID
    }

    @Override
    public List<PaymentOutBox> findAll() {
        return em.createQuery("SELECT p FROM PaymentOutBox p", PaymentOutBox.class)
                .getResultList(); // Fetch all entities
    }

    @Override
    public void deleteById(Long id) {
        PaymentOutBox paymentOutBox = findById(id); // Find the entity
        if (paymentOutBox != null) {
            em.remove(paymentOutBox); // Remove it if it exists
        }
    }

    @Override
    public List<PaymentOutBox> findUnprocessedEvents() {
        return em.createQuery("SELECT p FROM PaymentOutBox p WHERE p.processed = false", PaymentOutBox.class)
                .getResultList(); 
    }
}
