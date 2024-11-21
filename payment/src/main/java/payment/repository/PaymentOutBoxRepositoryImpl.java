package payment.repository;

import payment.domain.PaymentOutBox;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class PaymentOutBoxRepositoryImpl implements PaymentOutBoxRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public PaymentOutBox save(PaymentOutBox paymentOutBox) {
        em.persist(paymentOutBox);
        return paymentOutBox;
    }

    @Override
    public PaymentOutBox findById(UUID outBoxId) {
        return em.find(PaymentOutBox.class, outBoxId);
    }

    @Override
    public List<PaymentOutBox> findUnprocessedEvents() {
        
        return em.createQuery("SELECT p FROM PaymentOutBox p WHERE p.status = 'PENDING'", PaymentOutBox.class)
                 .getResultList();
    }

    @Override
    public void updateStatus(UUID outBoxId, String status) {
        PaymentOutBox paymentOutBox = findById(outBoxId);
        if (paymentOutBox != null) {
            paymentOutBox.setStatus(status);
            paymentOutBox.setProcessedAt(java.time.LocalDateTime.now());
            em.merge(paymentOutBox); 
        }
    }

    @Override
    public void deleteById(UUID outBoxId) {
        PaymentOutBox paymentOutBox = findById(outBoxId);
        if (paymentOutBox != null) {
            em.remove(paymentOutBox);
        }
    }
}
