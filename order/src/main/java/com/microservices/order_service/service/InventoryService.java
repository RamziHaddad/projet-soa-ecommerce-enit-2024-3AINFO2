package com.microservices.order_service.service;

import com.microservices.order_service.dto.AvailabilityCheckDTO;
import com.microservices.order_service.dto.OrderRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@Getter
@Setter
public class InventoryService {

    private final WebClient webClient;

    public InventoryService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083/api/inventory/checkOrder").build();
    }


    public Map<String, Object> checkOrderAvailability(AvailabilityCheckDTO availabilityCheckDTO) {
        String url = "http://localhost:8083/api/inventory/checkOrder";

        return webClient.post()
                .uri(url)
                .bodyValue(availabilityCheckDTO)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block(); // Communication synchrone
    }
}
