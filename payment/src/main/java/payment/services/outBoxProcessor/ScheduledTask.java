package payment.services.outBoxProcessor;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


@ApplicationScoped
public class ScheduledTask {

    @Inject
    PaymentOutBoxProcessor paymentOutBoxProcessor;

    // This will run every 10 seconds, you can adjust this interval as needed
    @Scheduled(every = "10s")
    void processPendingOutboxEvents() {
        paymentOutBoxProcessor.processOutboxEvents();
    }
}
