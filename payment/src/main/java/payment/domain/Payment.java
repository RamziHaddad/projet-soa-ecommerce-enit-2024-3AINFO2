package main.java.payment.domain;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import main.java.payment.domain.objectValues.PaymentMethod;
import main.java.payment.domain.objectValues.PaymentStatus;

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
    

}
