package payment.services.outBoxProcessor;

import java.io.StringReader;
import java.math.BigDecimal;
import java.util.List;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
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
    PaymentService paymentService;

    @Inject
    @RestClient
    BankClient bankClient;

    @Override
    @Retry(delay = 1000, maxRetries = 3) // Retry mechanism for failures
    @Timeout(1000) // 1s is the time out
    public Response processPaymentWithRetry(BankPaymentRequest request) {
        return bankClient.makeNewPayment(request);
    }
    int i = 0 ; 
    @Override
    @Scheduled(every = "10s")
    @Transactional
    public void processOutboxEvents() {
        List<PaymentOutBox> unprocessedEvents = boxRepository.findUnprocessedEvents();
        int maxEventsToProcess = 200;
       
    int processedCount = 0;
        for (PaymentOutBox event : unprocessedEvents) {
            if (processedCount >= maxEventsToProcess) {
                i++ ; 
                System.out.println("rate limiter"+i);
                break; // Exit loop if we have processed the maximum number of events
            }
            JsonObject payloadJson = Json.createReader(new StringReader(event.getPayload())).readObject();

            BigDecimal amount = payloadJson.getJsonNumber("amount").bigDecimalValue();
            int cardNumber = payloadJson.getInt("cardNumber");
            int cardCode = payloadJson.getInt("cardCode");
            PaymentStatus paymentStatus = PaymentStatus.valueOf(payloadJson.getString("paymentStatus"));

            Payment payment = paymentRepository.findById(event.getPaymentId());

            // Validate card details
            if (cardNumber != cardCode) {
                paymentService.cancelPayment(event.getPaymentId());
                event.setProcessed(true);
                boxRepository.update(event);
                processedCount++; 
                continue;
            }

            try {
                BankPaymentRequest bankPaymentRequest = new BankPaymentRequest(
                    event.getPaymentId(),
                    amount,
                    cardNumber,
                    cardCode
                );
                Response response = processPaymentWithRetry(bankPaymentRequest);
                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    paymentService.completePayment(payment.getPaymentId());
                }
                event.setProcessed(true);
                boxRepository.update(event);
                processedCount++; 

            } catch (Exception e) {

                paymentService.cancelPayment(event.getPaymentId());
                event.setProcessed(true);
                boxRepository.update(event);
                System.out.println(e);
                processedCount++; 
            }
        }
    }
}
