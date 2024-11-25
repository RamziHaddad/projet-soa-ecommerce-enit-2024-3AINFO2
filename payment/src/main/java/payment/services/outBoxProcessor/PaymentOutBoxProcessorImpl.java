package payment.services.outBoxProcessor;


import java.util.List;


import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.transaction.Transactional;
import payment.api.clients.BankClient;
import payment.api.clients.BankPaymentRequest;

import payment.domain.PaymentOutBox;
import payment.repository.outBoxRepository.PaymentOutBoxRepository;


@ApplicationScoped
public class PaymentOutBoxProcessorImpl implements PaymentOutBoxProcessor {

    @Inject
    PaymentOutBoxRepository boxRepository;

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
        try {
            // Step 1: Get the payload as a string
        BankPaymentRequest bankPaymentRequest = new BankPaymentRequest(event.getPaymentId(),event.getAmount(), event.getCardNumber(),event.getCardCode()) ; 
            bankClient.makeNewPayment(bankPaymentRequest) ; 
                
            return true ; 
        } catch (Exception e) {
            // TODO: handle exception
            event.setProcessed(true);
            event.setEventType("FAILED");
            System.out.println(event.toString());
            return false ; 
            
        }
    }
    
}
