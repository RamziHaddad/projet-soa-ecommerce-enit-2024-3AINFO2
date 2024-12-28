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
import payment.repository.PaymentRepository;
import payment.repository.outBoxRepository.PaymentOutBoxRepository;
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
        
        Payment payment = paymentMapper.toEntity(paymentRequest);

       
        payment.setPaymentStatus(PaymentStatus.PENDING);

      
        payment.setPaymentDate(LocalDateTime.now());
        Payment savedPayment = paymentRepository.savePayment(payment);

        CreditCardRequestDTO creditCardRequest = new CreditCardRequestDTO();
        creditCardRequest.setCustomerId(paymentRequest.getCustomerId());
        creditCardRequest.setCardCode(paymentRequest.getCardCode());
        creditCardRequest.setSecretNumber(paymentRequest.getCardNumber());
        cardServices.registerCreditCard(creditCardRequest);
        JsonObject payloadJson = Json.createObjectBuilder()
        .add("amount", paymentRequest.getAmount())
        .add("cardNumber", paymentRequest.getCardNumber())
        .add("cardCode", paymentRequest.getCardCode())
        .add("paymentStatus", payment.getPaymentStatus().name())
        .build();
        String payloadString = payloadJson.toString();
       
        PaymentOutBox outBoxEvent = new PaymentOutBox();
        outBoxEvent.setEventType("PAYMENT_CREATED");
        outBoxEvent.setPaymentId(savedPayment.getPaymentId());
        outBoxEvent.setPayload(payloadString);       
        outBoxEvent.setProcessed(false);

        
        boxRepository.save(outBoxEvent);

        
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

    @Override
    public boolean completePayment(UUID paymentUuid) {
        Payment payment=paymentRepository.findById(paymentUuid) ; 
        if (payment != null && payment.getPaymentStatus() == PaymentStatus.PENDING){
            payment.setPaymentStatus(PaymentStatus.COMPLETED);
            paymentRepository.updatePayment(payment) ; 
            return true ; 
        }
        return false ; 
    }
}
