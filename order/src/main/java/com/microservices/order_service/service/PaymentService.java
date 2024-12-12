package com.microservices.order_service.service;
import com.microservices.order_service.dto.PaymentRequestDTO;
import com.microservices.order_service.dto.PaymentResponseDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


@Service
@Getter
@Setter
public class PaymentService {
    private WebClient webClient;
    public PaymentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8085/payments").build();
    }

    public PaymentResponseDTO checkPrice(PaymentRequestDTO paymentRequestDTO) {


        return webClient.post()
                .uri("http://localhost:8085/payments")
                .bodyValue(paymentRequestDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PaymentResponseDTO>() {})
                .block(); // Communication synchrone
    }
}
