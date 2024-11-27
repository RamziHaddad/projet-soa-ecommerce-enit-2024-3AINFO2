package com.microservices.order_service.dto;

import com.microservices.order_service.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityCheckDTO {
    private UUID orderId;
    private List<Item> items;

}


