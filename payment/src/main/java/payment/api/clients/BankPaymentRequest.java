package payment.api.clients;

import java.math.BigDecimal;
import java.util.UUID;

public record BankPaymentRequest(
   UUID paymentId,BigDecimal amount,int cardNumber,int cardCode
) {}