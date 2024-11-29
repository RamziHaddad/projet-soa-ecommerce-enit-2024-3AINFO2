package payment.domain;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import payment.domain.objectValues.PaymentMethod;
import payment.domain.objectValues.PaymentStatus;
import java.util.Objects;

@Entity 
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentId ; 
    private UUID orderId ;
    private BigDecimal ammount ; 
    private PaymentStatus paymentStatus ; 
    
    private LocalDateTime paymentDate ; 
    private  UUID transactionId ; 
    private PaymentMethod paymentMethod ; 
    private UUID customerId ; 

    public UUID getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmmount() {
        return this.ammount;
    }

    public void setAmmount(BigDecimal ammount) {
        this.ammount = ammount;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public UUID getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public UUID getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public Payment() {
    }

    public Payment(UUID paymentId, UUID orderId, BigDecimal ammount, PaymentStatus paymentStatus, LocalDateTime paymentDate, UUID transactionId, PaymentMethod paymentMethod, UUID customerId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.ammount = ammount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.transactionId = transactionId;
        this.paymentMethod = paymentMethod;
        this.customerId = customerId;
    }

    public Payment paymentId(UUID paymentId) {
        setPaymentId(paymentId);
        return this;
    }

    public Payment orderId(UUID orderId) {
        setOrderId(orderId);
        return this;
    }

    public Payment ammount(BigDecimal ammount) {
        setAmmount(ammount);
        return this;
    }

    public Payment paymentStatus(PaymentStatus paymentStatus) {
        setPaymentStatus(paymentStatus);
        return this;
    }

    public Payment paymentDate(LocalDateTime paymentDate) {
        setPaymentDate(paymentDate);
        return this;
    }

    public Payment transactionId(UUID transactionId) {
        setTransactionId(transactionId);
        return this;
    }

    public Payment paymentMethod(PaymentMethod paymentMethod) {
        setPaymentMethod(paymentMethod);
        return this;
    }

    public Payment customerId(UUID customerId) {
        setCustomerId(customerId);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Payment)) {
            return false;
        }
        Payment payment = (Payment) o;
        return Objects.equals(paymentId, payment.paymentId) && Objects.equals(orderId, payment.orderId) && Objects.equals(ammount, payment.ammount) && Objects.equals(paymentStatus, payment.paymentStatus) && Objects.equals(paymentDate, payment.paymentDate) && Objects.equals(transactionId, payment.transactionId) && Objects.equals(paymentMethod, payment.paymentMethod) && Objects.equals(customerId, payment.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, orderId, ammount, paymentStatus, paymentDate, transactionId, paymentMethod, customerId);
    }

    @Override
    public String toString() {
        return "{" +
            " paymentId='" + getPaymentId() + "'" +
            ", orderId='" + getOrderId() + "'" +
            ", ammount='" + getAmmount() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            "}";
    }
    

}
