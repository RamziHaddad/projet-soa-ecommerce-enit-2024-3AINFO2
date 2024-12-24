package org.soa.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.soa.dto.ItemDTO;
import java.util.UUID;

@RegisterRestClient(configKey = "catalog-api")
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CatalogueClient {

    @GET
    @Path("/{id}")
    ItemDTO fetchItemDetails(@PathParam("id") UUID id);
}
