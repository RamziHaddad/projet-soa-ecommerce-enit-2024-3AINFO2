package payment.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import payment.api.clients.BankClient;
import payment.api.clients.BankPaymentRequest;
import payment.api.dto.CreditCardRequestDTO;
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
    @RestClient
    BankClient bankClient;
    @Inject
    PaymentOutBoxRepository boxRepository;

    @Inject
    PaymentMapper paymentMapper;

    @Inject
    CreditCardServices cardServices;

    @Override
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentRequest) {
        // 1. Change the payment DTO to an entity
        Payment payment = paymentMapper.toEntity(paymentRequest);

        // 2. Set the payment status to 'PENDING' initially
        payment.setPaymentStatus(PaymentStatus.PENDING);

        // 3. Save the payment to the database
        payment.setPaymentDate(LocalDateTime.now());
        Payment savedPayment = paymentRepository.savePayment(payment);

        // 4. Register the credit card (external call)
        CreditCardRequestDTO creditCardRequest = new CreditCardRequestDTO();
        creditCardRequest.setCustomerId(paymentRequest.getCustomerId());
        creditCardRequest.setCardCode(paymentRequest.getCardCode());
        creditCardRequest.setSecretNumber(paymentRequest.getCardNumber());
        cardServices.registerCreditCard(creditCardRequest);

        // 5. Create a JSON payload for the PaymentOutBox event
        JsonObject payload = Json.createObjectBuilder()
                .add("paymentId", savedPayment.getPaymentId().toString())
                .add("amount", savedPayment.getAmmount())
                .add("cardCode", creditCardRequest.getCardCode())
                .add("secretCode", creditCardRequest.getSecretNumber())
                .build();

        // 6. Create a PaymentOutBox entry for the Outbox pattern
        PaymentOutBox outBoxEvent = new PaymentOutBox();
        outBoxEvent.setEventType("PAYMENT_CREATED");
        outBoxEvent.setPayload(payload.toString()); // Convert JsonObject to string
        outBoxEvent.setProcessed(false);

        // 7. Save the PaymentOutBox event to the outbox table (part of the same
        // transaction)
        boxRepository.save(outBoxEvent);

        // 8. Return the response DTO
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
