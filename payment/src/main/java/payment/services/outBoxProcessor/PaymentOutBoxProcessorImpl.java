package payment.services.outBoxProcessor;


import java.util.List;


import org.eclipse.microprofile.rest.client.inject.RestClient;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import payment.api.clients.BankClient;
import payment.api.clients.BankPaymentRequest;
import payment.domain.Payment;
import payment.domain.PaymentOutBox;
import payment.domain.objectValues.PaymentStatus;
import payment.repository.PaymentRepository;
import payment.repository.outBoxRepository.PaymentOutBoxRepository;


@ApplicationScoped
public class PaymentOutBoxProcessorImpl implements PaymentOutBoxProcessor {

    @Inject
    PaymentOutBoxRepository boxRepository;
    @Inject 
    PaymentRepository paymentRepository ; 
    @Inject
    @RestClient
    BankClient bankClient;

    @Override
    @Transactional
    public void processOutboxEvents() {
        // 1. Fetch unprocessed outbox events
        List<PaymentOutBox> unprocessedEvents = boxRepository.findUnprocessedEvents();

        for (PaymentOutBox event : unprocessedEvents) {
            // 2. Send each event to the bank
            boolean success = sendPaymentToBank(event);

            // 3. Update the event's processed status based on success or failure
            event.setProcessed(success);
            System.out.println(event.toString());
            boxRepository.save(event);
        }
    }

    private boolean sendPaymentToBank(PaymentOutBox event) {
        Payment payment = paymentRepository.findById(event.getPaymentId())  ; 
        try {
            // sending the payload to the bank 
        BankPaymentRequest bankPaymentRequest = new BankPaymentRequest(event.getPaymentId(),event.getAmount(), event.getCardNumber(),event.getCardCode()) ; 
            Response bankResponse= bankClient.makeNewPayment(bankPaymentRequest) ; 
            
            if(bankResponse.getStatus()==Response.Status.OK.getStatusCode()){
                event.setPaymentStatus(PaymentStatus.COMPLETED) ;
                
                payment.setPaymentStatus(PaymentStatus.COMPLETED);
            }
            
            return true ; 
        } catch (Exception e) {
            event.setProcessed(true);
            event.setEventType("FAILED");
            payment.setPaymentStatus(PaymentStatus.FAILED);
            System.out.println(event.toString());
            return false ; 
        }
    }

    @Override
     @Scheduled(every = "10s")
    public void changingStatusOfProcessedEvents() {
        List<Payment> paymentsSuccessfullyCompleted=boxRepository.findCompletedPayments() ; 
        paymentsSuccessfullyCompleted.stream()
            .forEach(payment -> payment.setPaymentStatus(PaymentStatus.COMPLETED));
        List<Payment> paymentsFailed=boxRepository.findFailedPayments() ; 
        paymentsFailed.stream()
        .forEach(payment -> payment.setPaymentStatus(PaymentStatus.FAILED));
    }
    
}
