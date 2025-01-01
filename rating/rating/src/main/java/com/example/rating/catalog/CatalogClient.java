package com.example.rating.catalog;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "catalog-service", url = "http://localhost:8082")
public interface CatalogClient {

    @GetMapping("/products/{id}/available")
    boolean isProductAvailable(@PathVariable("id") UUID id);
}