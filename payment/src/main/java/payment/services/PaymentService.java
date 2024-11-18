package payment.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import payment.api.dto.PaymentRequestDTO;
import payment.api.dto.PaymentResponseDTO;

public interface PaymentService {
    
    PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest);

    
    Optional<PaymentResponseDTO> getPaymentById(UUID paymentId);

    
    boolean cancelPayment(UUID paymentId);

    
    List<PaymentResponseDTO> getAllPayments();

    
    List<PaymentResponseDTO> getPaymentsByDate(LocalDateTime date, UUID customerId);
}
