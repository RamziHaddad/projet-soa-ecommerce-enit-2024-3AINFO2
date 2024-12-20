package org.ecommerce.service;

import java.util.UUID;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@Path("/pricing")
@RegisterRestClient(configKey="pricing-api")
public interface PricingService {
    @GET
    @Path("/{id}")
    public double getProductPrice(@PathParam("id") UUID id);
}
