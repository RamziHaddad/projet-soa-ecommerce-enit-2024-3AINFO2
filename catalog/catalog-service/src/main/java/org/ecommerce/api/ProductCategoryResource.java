package org.ecommerce.api;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecommerce.domain.ProductCategory;
import org.ecommerce.exceptions.EntityAlreadyExistsException;
import org.ecommerce.service.ProductCategoryService;

import java.util.List;

@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductCategoryResource {

    @Inject
    ProductCategoryService categoryService;

    @POST
    public Response addCategory(ProductCategory category) {
        try {
            ProductCategory createdCategory = categoryService.addCategory(category);
            return Response.status(Response.Status.CREATED).entity(createdCategory).build();
        } catch (EntityAlreadyExistsException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @GET
    public Response getAllCategories() {
        List<ProductCategory> categories = categoryService.getAllCategories();
        return Response.ok(categories).build();
    }
}
