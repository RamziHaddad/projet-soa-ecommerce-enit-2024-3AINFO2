package org.ecommerce.api;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.ecommerce.domain.Product;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.exceptions.EntityNotFoundException;
import org.ecommerce.service.ProductService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService productService;

    @GET
    @Operation(summary = "Get all products", description = "Retrieves a list of all products.")
    public Response findAll() {
        List<Product> products = productService.findAll();
        return Response.ok(products).build();
    }

    @GET
    @Path("/page")
    @Operation(summary = "Get paginated products", description = "Retrieves a paginated list of products.")
    public Response findByRange(
        @QueryParam("page") @DefaultValue("0") int page,
        @QueryParam("range") @DefaultValue("10") int range) {
        List<Product> products = productService.findByRange(page * range, range);
        return Response.ok(products).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get product details", description = "Retrieves product details by ID.")
    public Response getProductDetails(
        @Parameter(name = "id", required = true, description = "ID of the product to retrieve")
        @PathParam("id") UUID id) {
        try {
            Product product = productService.getProductDetails(id);
            return Response.ok(product).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Operation(summary = "Create a new product with category", description = "Creates a new product with a specified category.")
    public Response addProductWithCategory(Product product, @QueryParam("categoryName") String categoryName) {
        try {
            Product createdProduct = productService.add(product, categoryName);
            URI uri = UriBuilder.fromResource(ProductResource.class).path("/{id}").resolveTemplate("id", createdProduct.getId()).build();
            return Response.created(uri).entity(createdProduct).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Category not found: " + categoryName).build();
        }
    }

    @PUT
    @Operation(summary = "Update a product", description = "Updates an existing product.")
    public Response updateProduct(Product product) throws EntityNotFoundException{
        try {
            Product updatedProduct = productService.updateProduct(product);
            return Response.ok(updatedProduct).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a product", description = "Deletes a product by ID.")
    public Response removeProduct(@PathParam("id") UUID id) {
            productService.removeProduct(id);
            return Response.noContent().build();
        
    }
}
