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
    @Retry(maxRetries = 3, delay = 500, jitter = 200) // Retry mechanism for failures
    @Timeout(1000) // Timeout after 1 second
    public Response processPaymentWithRetry(BankPaymentRequest request) {
        return bankClient.makeNewPayment(request);
    }

    @Override
    @Scheduled(every = "10s")
    @Transactional
    public void processOutboxEvents() {
        List<PaymentOutBox> unprocessedEvents = boxRepository.findUnprocessedEvents();

        for (PaymentOutBox event : unprocessedEvents) {
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
                continue;
            }

            try {
                BankPaymentRequest bankPaymentRequest = new BankPaymentRequest(
                    event.getPaymentId(),
                    amount,
                    cardNumber,
                    cardCode
                );

                // Process the payment with retry and timeout mechanisms
                Response response = processPaymentWithRetry(bankPaymentRequest);

                if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                    paymentService.completePayment(payment.getPaymentId());
                } else {
                    paymentService.cancelPayment(payment.getPaymentId());
                }

                // Mark the event as processed
                event.setProcessed(true);
                boxRepository.update(event);

            } catch (Exception e) {
                System.err.println("Error processing payment: " + e.getMessage());

                // Handle failure due to timeout or other exceptions
                paymentService.cancelPayment(event.getPaymentId());
                event.setProcessed(true);
                boxRepository.update(event);
            }
        }
    }
}
