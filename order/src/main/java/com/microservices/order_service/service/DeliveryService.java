package com.microservices.order_service.service;

import com.microservices.order_service.dto.OrderDeliveryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class DeliveryService {

    private final WebClient webClient;

    @Value("${shipping.service.url}")
    private String shippingServiceUrl;

    public DeliveryService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public void startDelivery(OrderDeliveryDTO orderDeliveryDTO) {
        webClient.post()
                .uri(shippingServiceUrl + "/startShipping")
                .body(Mono.just(orderDeliveryDTO), OrderDeliveryDTO.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
