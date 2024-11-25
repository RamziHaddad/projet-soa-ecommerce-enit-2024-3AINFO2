package payment.api.clients;

import java.math.BigDecimal;
import java.util.UUID;

public record BankPaymentRequest(
   UUID transactionId,BigDecimal amount,int cardNumber,int cardCode
) {}