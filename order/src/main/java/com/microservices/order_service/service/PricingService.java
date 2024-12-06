package com.microservices.order_service.service;

import com.microservices.order_service.dto.CartItem;
import com.microservices.order_service.dto.cartResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
public class PricingService {
    private WebClient webClient;
    public PricingService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8086/api/cart-total").build();
    }

    public cartResponse checkPrice(List<CartItem> cartItems) {


        return webClient.post()
                .uri("http://localhost:8086/api/cart-total")
                .bodyValue(cartItems)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<cartResponse>() {})
                .block(); // Communication synchrone
    }
}
