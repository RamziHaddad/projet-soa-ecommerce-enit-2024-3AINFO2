package org.ecommerce.service;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

//Interface for accessing product pricing information through REST API
//Defines method for retrieving base price of a product


@Path("/pricing")
@RegisterRestClient(configKey="pricing-api")
public interface PricingService {

    @GET
    @Path("/base-price/{id}")
    public BigDecimal getProductPrice(@PathParam("id") UUID id);

    

}
