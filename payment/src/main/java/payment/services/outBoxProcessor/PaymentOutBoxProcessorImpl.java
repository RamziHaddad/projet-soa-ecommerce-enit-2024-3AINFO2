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
import payment.services.PaymentService;

@ApplicationScoped
public class PaymentOutBoxProcessorImpl implements PaymentOutBoxProcessor {

    @Inject
    PaymentOutBoxRepository boxRepository;

    @Inject
    PaymentRepository paymentRepository;
    @Inject
    PaymentService paymentService ; 

    @Inject
    @RestClient
    BankClient bankClient;

    @Override
    @Scheduled(every = "10s")
    @Transactional
    public void processOutboxEvents() {
        
        List<PaymentOutBox> unprocessedEvents = boxRepository.findUnprocessedEvents();

        for (PaymentOutBox event : unprocessedEvents) {
            
                Payment payment = paymentRepository.findById(event.getPaymentId());
                BankPaymentRequest bankPaymentRequest = new BankPaymentRequest(
                        event.getPaymentId(),
                        event.getAmount(),
                        event.getCardNumber(),
                        event.getCardCode()
                );

                try {   
                Response response = bankClient.makeNewPayment(bankPaymentRequest) ; 
                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    event.setProcessed(true);
                    event.setPaymentStatus(PaymentStatus.COMPLETED);
                    boxRepository.update(event);
                    paymentService.completePayment(payment.getPaymentId()) ; 
                } 
            } catch (Exception e) {
                event.setProcessed(true);
                event.setPaymentStatus(PaymentStatus.FAILED);
                boxRepository.update(event); 
                
            }
        }
    }

    @Override
    public void processFailedEvents() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processFailedEvents'");
    }

}
