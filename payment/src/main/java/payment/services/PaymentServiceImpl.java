package payment.services;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import payment.api.dto.PaymentRequestDTO;
import payment.api.dto.PaymentResponseDTO;
import payment.domain.Payment;
import payment.domain.objectValues.PaymentStatus;
import payment.repository.PaymentRepository;
import payment.services.mappers.PaymentMapper;
@ApplicationScoped
public class PaymentServiceImpl implements PaymentService {

    @Inject
    PaymentRepository paymentRepository;

    @Inject
    PaymentMapper paymentMapper;

    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest) {
        
        Payment payment = paymentMapper.toEntity(paymentRequest);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());

        
        Payment savedPayment = paymentRepository.savePayment(payment);

        
        return paymentMapper.toResponseDTO(savedPayment);
    }

    @Override
    public Optional<PaymentResponseDTO> getPaymentById(UUID paymentId) {
        return Optional.ofNullable(paymentRepository.findById(paymentId))
                .map(paymentMapper::toResponseDTO);
    }

    @Override
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
