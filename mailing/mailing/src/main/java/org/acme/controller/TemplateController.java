
package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.Template;
import org.acme.service.TemplateService;

import java.util.List;

@Path("/templates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TemplateController {

    @Inject
    TemplateService templateService;

    @GET
    public List<Template> getAllTemplates() {
        return templateService.getAllTemplates();
    }

    @GET
    @Path("/{id}")
    public Response getTemplateById(@PathParam("id") Long id) {
        Template template = templateService.getTemplateById(id);
        if (template == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Template not found").build();
        }
        return Response.ok(template).build();
    }

    @POST
    @Transactional
    public Response createTemplate(Template template) {
        Template createdTemplate = templateService.createTemplate(template);
        return Response.status(Response.Status.CREATED).entity(createdTemplate).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateTemplate(@PathParam("id") Long id, Template updatedTemplate) {
        try {
            Template template = templateService.updateTemplate(id, updatedTemplate);
            return Response.ok(template).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTemplate(@PathParam("id") Long id) {
        if (templateService.deleteTemplate(id)) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Template not found").build();
        }
    }
}

