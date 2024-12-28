package payment.domain;




import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import payment.domain.objectValues.PaymentStatus;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
public class PaymentOutBox {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String eventType;
    private UUID paymentId;
    
    private boolean processed;
    private String payload ; 

    public PaymentOutBox(Long id, String eventType, UUID paymentId, boolean processed, String payload) {
        this.id = id;
        this.eventType = eventType;
        this.paymentId = paymentId;
        this.processed = processed;
        this.payload = payload;
    }

    public PaymentOutBox() {
    }

    

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return this.eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public UUID getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    

   

    public boolean isProcessed() {
        return this.processed;
    }

    public boolean getProcessed() {
        return this.processed;
    }

    public void setProcessed(boolean processed) {
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

    public PaymentOutBox paymentId(UUID paymentId) {
        setPaymentId(paymentId);
        return this;
    }

    


    public PaymentOutBox processed(boolean processed) {
        setProcessed(processed);
        return this;
    }
    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PaymentOutBox)) {
            return false;
        }
        PaymentOutBox paymentOutBox = (PaymentOutBox) o;
        return Objects.equals(id, paymentOutBox.id) && Objects.equals(eventType, paymentOutBox.eventType) && Objects.equals(paymentId, paymentOutBox.paymentId) && processed == paymentOutBox.processed;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, paymentId, processed);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", paymentId='" + getPaymentId() + "'" +
            ", processed='" + isProcessed() + "'" +
            "}";
    }
    

}
