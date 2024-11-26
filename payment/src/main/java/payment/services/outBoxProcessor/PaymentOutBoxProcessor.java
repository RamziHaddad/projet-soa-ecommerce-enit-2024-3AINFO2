package payment.services.outBoxProcessor;

public interface PaymentOutBoxProcessor {
    void processOutboxEvents(); 
    void changingStatusOfProcessedEvents() ; 
}
