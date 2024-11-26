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
    private BigDecimal amount;
    private int cardNumber;
    private int cardCode;
    private PaymentStatus paymentStatus ; 
    private boolean processed;

    public PaymentOutBox() {
    }

    public PaymentOutBox(Long id, String eventType, UUID paymentId, BigDecimal amount, int cardNumber, int cardCode, PaymentStatus paymentStatus, boolean processed) {
        this.id = id;
        this.eventType = eventType;
        this.paymentId = paymentId;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.cardCode = cardCode;
        this.paymentStatus = paymentStatus;
        this.processed = processed;
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

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCardNumber() {
        return this.cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCardCode() {
        return this.cardCode;
    }

    public void setCardCode(int cardCode) {
        this.cardCode = cardCode;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public PaymentOutBox amount(BigDecimal amount) {
        setAmount(amount);
        return this;
    }

    public PaymentOutBox cardNumber(int cardNumber) {
        setCardNumber(cardNumber);
        return this;
    }

    public PaymentOutBox cardCode(int cardCode) {
        setCardCode(cardCode);
        return this;
    }

    public PaymentOutBox paymentStatus(PaymentStatus paymentStatus) {
        setPaymentStatus(paymentStatus);
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
        return Objects.equals(id, paymentOutBox.id) && Objects.equals(eventType, paymentOutBox.eventType) && Objects.equals(paymentId, paymentOutBox.paymentId) && Objects.equals(amount, paymentOutBox.amount) && cardNumber == paymentOutBox.cardNumber && cardCode == paymentOutBox.cardCode && Objects.equals(paymentStatus, paymentOutBox.paymentStatus) && processed == paymentOutBox.processed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventType, paymentId, amount, cardNumber, cardCode, paymentStatus, processed);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", paymentId='" + getPaymentId() + "'" +
            ", amount='" + getAmount() + "'" +
            ", cardNumber='" + getCardNumber() + "'" +
            ", cardCode='" + getCardCode() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", processed='" + isProcessed() + "'" +
            "}";
    }
    

}
