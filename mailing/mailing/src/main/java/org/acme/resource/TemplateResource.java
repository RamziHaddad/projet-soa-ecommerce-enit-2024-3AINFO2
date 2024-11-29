package org.acme.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.Template;
import org.acme.service.TemplateService;

import java.util.Map;

@Path("/templates") // Define the base path for all endpoints
@Produces(MediaType.APPLICATION_JSON) // Set default response type
@Consumes(MediaType.APPLICATION_JSON) // Set default request type
public class TemplateResource {

    private final TemplateService templateService = new TemplateService(); // Assuming you have a service layer for business logic

    // Endpoint to create a new template
    @POST
    @Path("/create")
    public Response createTemplate(Template template) {
        try {
            String templateId = templateService.createTemplate(template); // Call service to create and save the template
            return Response.status(Response.Status.CREATED).entity(templateId).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create template: " + e.getMessage()).build();
        }
    }

    // Endpoint to fetch an email template by ID
    @GET
    @Path("/{id}")
    public Response getTemplate(@PathParam("id") int templateId) {
        try {
            Template template = templateService.getTemplateById(templateId); // Fetch template from service
            return Response.ok(template).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Template not found: " + e.getMessage()).build();
        }
    }
}
