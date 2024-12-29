package com.microservices.order_service.service;


import com.microservices.order_service.dto.Address;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

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
    public List<Address> getUserAddresses(UUID userId) {
        return webClient.get()
                .uri(shippingServiceUrl +"/"+userId)
                .retrieve()
                .bodyToFlux(Address.class)
                .collectList()
                .block();
    }


}
