package payment.repository.outBoxRepository;

import payment.domain.Payment;
import payment.domain.PaymentOutBox;
import java.util.List;

public interface PaymentOutBoxRepository {

    PaymentOutBox save(PaymentOutBox paymentOutBox);

    PaymentOutBox update(PaymentOutBox paymentOutBox);

    PaymentOutBox findById(Long id);

    List<PaymentOutBox> findAll();

    void deleteById(Long id);

    List<PaymentOutBox> findUnprocessedEvents();
    List<PaymentOutBox> findCompletedEvents() ; 
    List<Payment> findCompletedPayments() ; 
    List<Payment> findFailedPayments() ; 
}
