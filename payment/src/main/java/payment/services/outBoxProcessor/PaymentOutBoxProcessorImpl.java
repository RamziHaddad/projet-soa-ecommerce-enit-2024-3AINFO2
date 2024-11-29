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
            if(success){
                event.setProcessed(success);
                boxRepository.update(event); 
            }else{
                event.setProcessed(true);
                event.setPaymentStatus(PaymentStatus.FAILED); 
                boxRepository.update(event) ; 
                Payment payment=paymentRepository.findById(event.getPaymentId()) ; 
                
                payment.setPaymentStatus(PaymentStatus.FAILED) ; 
                paymentRepository.updatePayment(payment) ; 
                System.out.println("your payment is "+paymentRepository.findById(payment.getPaymentId()));
            }

            // 3. Update the event's processed status based on success or failure
            
            
        }
    }
    @Transactional
    public boolean sendPaymentToBank(PaymentOutBox event) {
        Payment payment = paymentRepository.findById(event.getPaymentId());
    
        // Create the request object with necessary details
        BankPaymentRequest bankPaymentRequest = new BankPaymentRequest(
            event.getPaymentId(),
            event.getAmount(),
            event.getCardNumber(),
            event.getCardCode()
        );
    
        try {
            // Send the payment request
            Response bankResponse = bankClient.makeNewPayment(bankPaymentRequest);
    
            // Check the status of the response
            switch (bankResponse.getStatus()) {
                case 200: // Response.Status.OK
                    event.setPaymentStatus(PaymentStatus.COMPLETED);
                    payment.setPaymentStatus(PaymentStatus.COMPLETED);
                    return true;
    
            }
    
        } catch (Exception e) {
            // Handle unexpected exceptions
            return false ; 
        }
        return true;
    }
    
}
