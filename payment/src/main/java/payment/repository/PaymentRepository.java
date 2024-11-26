package payment.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import payment.domain.Payment;

public interface PaymentRepository {
    public Payment savePayment (Payment payment ); 
    public Payment findById (UUID id ); 
    public Boolean deletePayment (UUID id); 
    public List<Payment> findAll ();
    public List<Payment> findByDate (LocalDateTime date , UUID id ); 
    
}
