package com.microservices.order_service.service;
import com.microservices.order_service.dto.CartItem;
import com.microservices.order_service.dto.PaymentRequestDTO;
import com.microservices.order_service.dto.PaymentResponseDTO;
import com.microservices.order_service.dto.cartResponse;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
@Getter
@Setter
public class PaymentService {
    private WebClient webClient;
    public PaymentService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8085/payments").build();
    }
    @Autowired
    private PricingService pricingService;
    public PaymentResponseDTO processPayment(List<CartItem> cartItems, @NotNull PaymentRequestDTO paymentRequestDTO) {

        cartResponse pricingResponse = pricingService.checkPrice(cartItems);
        BigDecimal totalAmount = pricingResponse.getTotalBeforeDiscount();


        paymentRequestDTO.setAmount(totalAmount);
        return sendForPayment(paymentRequestDTO);
    }


    public PaymentResponseDTO sendForPayment(PaymentRequestDTO paymentRequestDTO) {
        return webClient.post()
                .uri("http://localhost:8085/payments")
                .bodyValue(paymentRequestDTO)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                    return clientResponse.createException()
                            .flatMap(error -> Mono.error(new RuntimeException("Client Error: " + error.getMessage())));
                })
                .onStatus(status -> status.is5xxServerError(), clientResponse -> {
                    return clientResponse.createException()
                            .flatMap(error -> Mono.error(new RuntimeException("Server Error: " + error.getMessage())));
                })
                .bodyToMono(new ParameterizedTypeReference<PaymentResponseDTO>() {})
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(10))
                        .filter(throwable -> throwable instanceof RuntimeException))
                // Adding timeout
                .timeout(Duration.of(60, ChronoUnit.SECONDS))
                .block(); // Communication is synchronous here
    }

    public List<PaymentResponseDTO> getAllPayments() {
        return webClient.get()
                .uri("http://localhost:8085/payments")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<PaymentResponseDTO>>() {})
                .block();

    }
}
