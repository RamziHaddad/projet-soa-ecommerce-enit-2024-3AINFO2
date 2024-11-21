package payment.repository;

import payment.domain.PaymentOutBox;
import java.util.List;
import java.util.UUID;

public interface PaymentOutBoxRepository {

    
    PaymentOutBox save(PaymentOutBox paymentOutBox);

    
    PaymentOutBox findById(UUID outBoxId);

    
    List<PaymentOutBox> findUnprocessedEvents();

    
    void updateStatus(UUID outBoxId, String status);

    
    void deleteById(UUID outBoxId);
}
