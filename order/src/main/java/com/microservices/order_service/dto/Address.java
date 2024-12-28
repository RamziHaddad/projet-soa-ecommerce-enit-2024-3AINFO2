package com.microservices.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class Address {

    private UUID addressId;

    private UUID userId;

    private String street;

    private String postalCode;


    private String city;


    private String country;


}
