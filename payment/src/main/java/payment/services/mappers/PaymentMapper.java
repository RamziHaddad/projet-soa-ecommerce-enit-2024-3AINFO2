package payment.services.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import payment.api.dto.PaymentRequestDTO;
import payment.api.dto.PaymentResponseDTO;
import payment.domain.Payment;

@ApplicationScoped
public class PaymentMapper {
     private final Jsonb jsonb = JsonbBuilder.create();
    // Convert PaymentRequestDTO to Payment entity
    public Payment toEntity(PaymentRequestDTO dto) {
        Payment payment = new Payment();
        payment.setOrderId(dto.getOrderId());
        payment.setAmmount(dto.getAmount());
        payment.setCustomerId(dto.getCustomerId());
        payment.setPaymentMethod(dto.getPaymentMethod());
        return payment;
    }

    // Convert Payment entity to PaymentResponseDTO
    public PaymentResponseDTO toResponseDTO(Payment payment) {
        return new PaymentResponseDTO(
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getAmmount(),
                payment.getPaymentStatus(),
                payment.getPaymentDate(),
                payment.getCustomerId()
        );
    }

    public String toJson(Payment payment) {
        return jsonb.toJson(payment);
    }
}
