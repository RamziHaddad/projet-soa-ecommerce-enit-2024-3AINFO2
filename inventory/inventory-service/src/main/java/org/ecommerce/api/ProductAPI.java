package org.ecommerce.api;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.ecommerce.model.Product;
import org.ecommerce.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/products")
public class ProductAPI {
    @Inject
    ProductService productService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        return Response.ok(productService.getAllProducts()).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewProduct(Product product) throws JsonProcessingException {
        productService.addNewProduct(product);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("id") UUID id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeProductById(@PathParam("id") UUID id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            productService.removeProductById(id);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductById(@PathParam("id") UUID id, Product updatedProduct) throws JsonProcessingException {
        Optional<Product> product = productService.getProductById(id);
        updatedProduct.setId(id);
        if (product.isPresent()) {
            return Response.ok(productService.updateProduct(id, updatedProduct)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/reception")
    public Product registerReception(Map<String, Object> payload) throws JsonProcessingException {
        String productIdStr = (String) payload.get("productId");
        UUID productId = UUID.fromString(productIdStr);
        Object quantityObj = payload.get("quantity");
        int quantity = (quantityObj instanceof BigDecimal) ? ((BigDecimal) quantityObj).intValue() : (int) quantityObj;
        return productService.registerReception(productId, quantity);
    }

    @POST
    @Path("/reserve")
    public Product reserveProduct(Map<String, Object> payload) throws JsonProcessingException {
        String productIdStr = (String) payload.get("productId");
        UUID productId = UUID.fromString(productIdStr);
        Object quantityObj = payload.get("quantity");
        int quantity = (quantityObj instanceof BigDecimal) ? ((BigDecimal) quantityObj).intValue() : (int) quantityObj;
        return productService.reserveProduct(productId, quantity);
    }



    @POST
    @Path("/release")
    public Product releaseReservation(Map<String, Object> payload) throws JsonProcessingException {
        String productIdStr = (String) payload.get("productId");
        UUID productId = UUID.fromString(productIdStr);
        Object quantityObj = payload.get("quantity");
        int quantity = (quantityObj instanceof BigDecimal) ? ((BigDecimal) quantityObj).intValue() : (int) quantityObj;
        return productService.releaseReservation(productId, quantity);
    }

    @POST
    @Path("/shipment")
    public Product recordOrderShipment(Map<String, Object> payload) throws JsonProcessingException {
        String productIdStr = (String) payload.get("productId");
        UUID productId = UUID.fromString(productIdStr);
        Object quantityObj = payload.get("quantity");
        int quantity = (quantityObj instanceof BigDecimal) ? ((BigDecimal) quantityObj).intValue() : (int) quantityObj;
        return productService.recordOrderShipment(productId, quantity);
    }
}
