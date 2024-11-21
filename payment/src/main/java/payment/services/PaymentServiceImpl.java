package payment.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import payment.api.dto.PaymentRequestDTO;
import payment.api.dto.PaymentResponseDTO;
import payment.domain.Payment;
import payment.domain.PaymentOutBox;
import payment.domain.objectValues.PaymentStatus;
import payment.repository.PaymentOutBoxRepository;
import payment.repository.PaymentRepository;
import payment.services.mappers.PaymentMapper;

@ApplicationScoped
public class PaymentServiceImpl implements PaymentService {

    @Inject
    PaymentRepository paymentRepository;
    
    @Inject 
    PaymentOutBoxRepository boxRepository; 
    
    @Inject
    PaymentMapper paymentMapper;

    @Override
@Transactional
public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest) {
    // 1. Convert DTO to domain entity (Payment)
    Payment payment = paymentMapper.toEntity(paymentRequest);

    // 2. Set the payment status to 'PENDING' initially
    payment.setPaymentStatus(PaymentStatus.PENDING);

    // 3. Save the payment to the database
    payment.setPaymentDate(LocalDateTime.now());
    Payment savedPayment = paymentRepository.savePayment(payment);

    // 4. Create a PaymentOutBox entry for the Outbox pattern
    PaymentOutBox outBoxEvent = new PaymentOutBox();
    outBoxEvent.setEventType("PAYMENT_CREATED");
    outBoxEvent.setPayload(String.format("Payment ID: %s, Amount: %.2f", savedPayment.getPaymentId(), savedPayment.getAmmount()));
    outBoxEvent.setProcessed(false);
    System.out.println(outBoxEvent.toString());  
    // 5. Save the PaymentOutBox event to the outbox table
    boxRepository.save(outBoxEvent);

    // 6. Return the response DTO
    return paymentMapper.toResponseDTO(savedPayment);
}


    @Override
    public Optional<PaymentResponseDTO> getPaymentById(UUID paymentId) {
        return Optional.ofNullable(paymentRepository.findById(paymentId))
                .map(paymentMapper::toResponseDTO);
    }

    @Override
    @Transactional
    public boolean cancelPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId);
        if (payment != null && payment.getPaymentStatus() == PaymentStatus.PENDING) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.savePayment(payment);
            return true;
        }
        return false;
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsByDate(LocalDateTime date, UUID customerId) {
        return paymentRepository.findByDate(date, customerId)
                .stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
