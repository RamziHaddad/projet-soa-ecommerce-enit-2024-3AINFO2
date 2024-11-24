package payment.api.dto;

import java.math.BigDecimal;
import java.util.UUID;
import payment.domain.objectValues.PaymentMethod;

public class PaymentRequestDTO {
    private UUID orderId;
    private BigDecimal amount;
    private UUID customerId;
    private PaymentMethod paymentMethod;
    private int cardNumber;
    private int cardCode ; 


    public PaymentRequestDTO() {
    }

    public PaymentRequestDTO(UUID orderId, BigDecimal amount, UUID customerId, PaymentMethod paymentMethod, int cardNumber, int cardCode) {
        this.orderId = orderId;
        this.amount = amount;
        this.customerId = customerId;
        this.paymentMethod = paymentMethod;
        this.cardNumber = cardNumber;
        this.cardCode = cardCode;
    }


    // Getters and Setters

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
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


}
