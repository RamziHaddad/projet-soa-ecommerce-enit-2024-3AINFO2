package com.microservices.order_service.service;

import com.microservices.order_service.dto.AddressDTO;
import com.microservices.order_service.dto.OrderDeliveryDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class DeliveryService {

    private final WebClient webClient;

    @Value("${delivery.service.url}")
    private String shippingServiceUrl;

    public DeliveryService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    /**
     * Fetches the list of user addresses from the shipping service.
     *
     * @return List of AddressDTO
     */
    public List<AddressDTO> getUserAddresses() {
        return webClient.get()
                .uri(shippingServiceUrl)
                .retrieve()
                .bodyToFlux(AddressDTO.class)
                .collectList()
                .block();
    }


}
