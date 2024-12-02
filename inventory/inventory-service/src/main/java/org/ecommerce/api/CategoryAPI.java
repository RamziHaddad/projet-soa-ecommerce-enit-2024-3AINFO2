package org.ecommerce.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.ecommerce.model.Category;
import org.ecommerce.repository.CategoryRepository;

import java.util.Optional;
import java.util.UUID;

@Path("/categories")
@ApplicationScoped
public class CategoryAPI {
    @Inject
    CategoryRepository categoryRepository;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCategories() {
        return Response.ok(categoryRepository.listAll()).build();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewCategory(Category category) throws JsonProcessingException {
        categoryRepository.addCategory(category);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategoryById(@PathParam("id") UUID id) {
        Optional<Category> category = categoryRepository.getCategoryByID(id);
        return category.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @DELETE
    @Path("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response removeCategoryById(@PathParam("id") UUID id) {
        Optional<Category> category = categoryRepository.getCategoryByID(id);
        if (category.isPresent()) {
            categoryRepository.deleteCategoryById(id);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategoryById(@PathParam("id") UUID id, Category updatedCategory) throws JsonProcessingException {
        Optional<Category> category = categoryRepository.getCategoryByID(id);
        updatedCategory.setId(id);
        if (category.isPresent()) {
            return Response.ok(categoryRepository.updateCategory(updatedCategory)).build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
