package org.enit.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.enit.model.Product;
import org.enit.repository.ProductRepository;

import java.util.Optional;
import java.util.UUID;

@Path("/products")
public class ProductAPI {
    @Inject
    ProductRepository productRepository;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts(){
        return Response.ok(productRepository.listAll()).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewProduct(Product product){
        productRepository.addProduct(product);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductById(@PathParam("id") UUID id){
        return Response.ok(productRepository.getProductByID(id)).build();
    }


    @DELETE
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeProductById(@PathParam("id") UUID id){
        productRepository.deleteProductById(id);
        return Response.status(Response.Status.OK).build();
    }



    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProductById(@PathParam("id") UUID id,Product updatedProduct){
        Optional<Product> product = productRepository.getProductByID(id);
        updatedProduct.setId(id);
        if (product.isPresent()) {

            return Response.ok( productRepository.updateProduct(updatedProduct)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }


}
