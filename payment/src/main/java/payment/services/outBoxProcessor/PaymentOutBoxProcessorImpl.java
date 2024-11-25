package payment.services.outBoxProcessor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import payment.api.clients.BankClient;
import payment.api.clients.BankPaymentRequest;
import payment.domain.PaymentOutBox;
import payment.repository.outBoxRepository.PaymentOutBoxRepository;
import payment.services.outBoxProcessor.PaymentOutBoxProcessor;

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
            boxRepository.save(event);
        }
    }

    private boolean sendPaymentToBank(PaymentOutBox event) {
        try {
           // Deserialize the JSON payload into a JsonObject
        Jsonb jsonb = JsonbBuilder.create();
        JsonObject jsonPayload = jsonb.fromJson(event.getPayload(), JsonObject.class);

        // Extract and validate fields from the JSON payload
        String paymentIdString = jsonPayload.containsKey("paymentId")
            ? jsonPayload.getString("paymentId", null)
            : null;
        if (paymentIdString == null) {
            throw new IllegalArgumentException("Missing or invalid 'transactionId'");
        }
        UUID paymentId = UUID.fromString(paymentIdString);

        BigDecimal amount = jsonPayload.containsKey("amount")
            && jsonPayload.getJsonNumber("amount") != null
            ? jsonPayload.getJsonNumber("amount").bigDecimalValue()
            : null;
        if (amount == null) {
            throw new IllegalArgumentException("Missing or invalid 'amount'");
        }

        // Validate and log cardNumber
        Integer cardNumber = jsonPayload.containsKey("cardNumber")
            && jsonPayload.getJsonNumber("cardNumber") != null
            ? jsonPayload.getJsonNumber("cardNumber").intValue()
            : null;
        if (cardNumber == null) {
            System.err.println("Error: Missing or invalid 'cardNumber' in the payload: " + jsonPayload);
            throw new IllegalArgumentException("Missing or invalid 'cardNumber'");
        }

        // Validate and log cardCode
        Integer cardCode = jsonPayload.containsKey("cardCode")
            && jsonPayload.getJsonNumber("cardCode") != null
            ? jsonPayload.getJsonNumber("cardCode").intValue()
            : null;
        if (cardCode == null) {
            System.err.println("Error: Missing or invalid 'cardCode' in the payload: " + jsonPayload);
            throw new IllegalArgumentException("Missing or invalid 'cardCode'");
        }
        int cardNum= cardNumber.intValue() ; 
        int cardCod=cardCode.intValue() ; 

        // Create a new BankPaymentRequest using the extracted variables
        BankPaymentRequest paymentRequest = new BankPaymentRequest(paymentId, amount, cardNum, cardCod);

        // Log the paymentRequest for debugging
        System.out.println("Created PaymentRequest: " + paymentRequest);

        // Call the bank client to process the payment
        bankClient.makeNewPayment(paymentRequest);

        // If successful, return true
        return true;
        } catch (Exception e) {
            // Log the exception details for debugging
            System.err.println("Error processing payment for event ID: " + event.getId());
            e.printStackTrace();
    
            // If an error occurs, return false
            return false;
        }
    }
    
}
