package com.microservices.order_service.service;

import com.microservices.order_service.dto.CartItem;
import com.microservices.order_service.dto.cartResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
public class PricingService {
    private WebClient webClient;
    public PricingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8086/pricing/purchase-total").build();
    }

    public cartResponse checkPrice(List<CartItem> cartItems) {


        return webClient.post()
                .uri("http://localhost:8086/pricing/purchase-total")
                .bodyValue(cartItems)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                    return clientResponse.createException()
                            .flatMap(error -> Mono.error(new RuntimeException("Client Error: " + error.getMessage())));
                })
                .onStatus(status -> status.is5xxServerError(), clientResponse -> {
                    return clientResponse.createException()
                            .flatMap(error -> Mono.error(new RuntimeException("Server Error: " + error.getMessage())));
                })
                .bodyToMono(new ParameterizedTypeReference<cartResponse>() {})
                .block(); // Communication synchrone
    }
}
