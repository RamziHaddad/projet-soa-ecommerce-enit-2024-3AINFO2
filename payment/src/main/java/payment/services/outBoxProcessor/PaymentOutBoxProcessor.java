package payment.services.outBoxProcessor;

import jakarta.ws.rs.core.Response;
import payment.api.clients.BankPaymentRequest;

public interface PaymentOutBoxProcessor {
    public  void processOutboxEvents(); 
    public Response processPaymentWithRetry(BankPaymentRequest request) ; 
    
}
