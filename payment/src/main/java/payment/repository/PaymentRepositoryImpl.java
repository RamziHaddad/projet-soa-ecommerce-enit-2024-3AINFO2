package main.java.payment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.PersistenceContext;
import jakarta.transaction.Transactional; 
import payment.domain.Payment;

public class PaymentRepositoryImpl implements PaymentRepository {
    @PersistenceContext
    EntityManager em; 

    @Transactional
    @Override
    public Boolean deletePayment(UUID id ) {
        Payment payment = em.find(Payment.class,id);
        if ( payment != null ){
            em.remove(payment); 
            return true ; 
        }
        return false;
    }

    @Override
    public List<Payment> findAll() {
        
        return em.createQuery("SELECT p FROM Payment p", Payment.class)
        .getResultList();
    }

    @Override
    public List<Payment> findByDate(LocalDateTime date, UUID id) {
        
        return em.createQuery("SELECT p FROM Payment p WHERE p.timestamp = :date AND p.id = :id", Payment.class)
                .setParameter("date", date)
                .setParameter("id", id)
                .getResultList();
    
    }

    @Override
    public Payment findById(UUID id) {
        Payment payment = em.find(Payment.class,id);
        return payment;
    }
    @Transactional
    @Override
    public Payment savePayment(Payment payment) {
        if (payment.getId() == null) {
            payment.setId(UUID.randomUUID()); 
        }
        em.persist(payment);
        return payment;
    }
    
    
}
