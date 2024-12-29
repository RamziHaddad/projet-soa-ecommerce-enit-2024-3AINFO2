package com.microservices.order_service.service;

import com.microservices.order_service.dto.AvailabilityCheckDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Service
@Getter
@Setter
public class InventoryService {

    private final WebClient webClient;

    public InventoryService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083/products/checkOrder").build();
    }


    public Map<String, Object> checkOrderAvailability(AvailabilityCheckDTO availabilityCheckDTO) {
        String url = "http://localhost:8083/products/checkOrder";

        return webClient.post()
                .uri(url)
                .bodyValue(availabilityCheckDTO)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), clientResponse -> {
                    return clientResponse.createException()
                            .flatMap(error -> Mono.error(new RuntimeException("Client Error: " + error.getMessage())));
                })
                .onStatus(status -> status.is5xxServerError(), clientResponse -> {
                    return clientResponse.createException()
                            .flatMap(error -> Mono.error(new RuntimeException("Server Error: " + error.getMessage())));
                })
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(10))
                        .filter(throwable -> throwable instanceof RuntimeException))
                // Adding timeout
                .timeout(Duration.of(60, ChronoUnit.SECONDS))
                .block(); // Communication synchrone
    }
}
