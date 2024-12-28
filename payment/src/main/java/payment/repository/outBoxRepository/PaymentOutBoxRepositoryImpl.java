package payment.repository.outBoxRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import payment.domain.Payment;
import payment.domain.PaymentOutBox;
import payment.domain.objectValues.PaymentStatus;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class PaymentOutBoxRepositoryImpl implements PaymentOutBoxRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public PaymentOutBox save(PaymentOutBox paymentOutBox) {
        em.persist(paymentOutBox); // Persist the entity into the database
        return paymentOutBox;
    }

    @Override
    @Transactional
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

    @Override
    public List<PaymentOutBox> findCompletedEvents() {
        return em.createQuery("SELECT p FROM PaymentOutBox p WHERE p.processed = true", PaymentOutBox.class)
                .getResultList();
    }

    @Override
    public List<Payment> findCompletedPayments() {

       return em.createQuery(
            "SELECT p FROM Payment p WHERE p.paymentId IN " +
            "(SELECT po.paymentId FROM PaymentOutBox po WHERE po.paymentStatus = :status AND po.processed = true)", 
            Payment.class)
            .setParameter("status", PaymentStatus.COMPLETED)
            .getResultList();
    }

    @Override
    public List<Payment> findFailedPayments() {
        return em.createQuery(
            "SELECT p FROM Payment p WHERE p.paymentId IN " +
            "(SELECT po.paymentId FROM PaymentOutBox po WHERE po.paymentStatus = :status AND po.processed = true)", 
            Payment.class)
            .setParameter("status", PaymentStatus.FAILED)
            .getResultList();
    }

    @Override
    public PaymentOutBox findByPaymentId(UUID transactionId) {
        try {
            return em.createQuery(
                "SELECT po FROM PaymentOutBox po WHERE po.paymentId = :transactionId", PaymentOutBox.class)
                .setParameter("transactionId", transactionId)
                .getSingleResult();
        } catch (Exception e) {
            // Log the exception if necessary and return null if no result is found
            return null;
        }
    }
}
