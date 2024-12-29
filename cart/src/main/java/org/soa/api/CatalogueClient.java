package org.soa.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.soa.dto.ItemDTO;
import java.util.UUID;

@RegisterRestClient(configKey = "catalog-api") // Correspond à la clé de configuration
@Path("/products") // Correspond à l'URI de base
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CatalogueClient {

    // Récupérer les détails d'un item
    @GET
    @Path("/{id}")
    ItemDTO fetchItemDetails(@PathParam("id") UUID id);
}
