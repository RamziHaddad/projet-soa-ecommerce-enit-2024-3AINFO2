package payment.domain;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class PaymentOutBox {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String eventType;
    private String payload;
    private boolean processed;

    

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getProcessed() {
        return this.processed;
    }


    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public PaymentOutBox() {
    }

    public PaymentOutBox(Long id, String eventType, String payload, boolean processed) {
        this.id = id;
        this.eventType = eventType;
        this.payload = payload;
        this.processed = processed;
    }

    public PaymentOutBox id(Long id) {
        setId(id);
        return this;
    }

    public PaymentOutBox eventType(String eventType) {
        setEventType(eventType);
        return this;
    }

    public PaymentOutBox payload(String payload) {
        setPayload(payload);
        return this;
    }

    public PaymentOutBox processed(boolean processed) {
        setProcessed(processed);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PaymentOutBox)) {
            return false;
        }
        PaymentOutBox paymentOutBox = (PaymentOutBox) o;
        return Objects.equals(id, paymentOutBox.id) && Objects.equals(eventType, paymentOutBox.eventType) && Objects.equals(payload, paymentOutBox.payload) && processed == paymentOutBox.processed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, payload, processed);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", payload='" + getPayload() + "'" +
            ", processed='" + isProcessed() + "'" +
            "}";
    }
}
