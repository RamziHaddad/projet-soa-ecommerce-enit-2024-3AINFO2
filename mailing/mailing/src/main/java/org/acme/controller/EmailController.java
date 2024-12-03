
package org.acme.controller;

import org.acme.model.EmailRequest;
//import org.acme.model.Template;
import org.acme.service.MailtrapService;
import org.acme.service.TemplateService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
//import java.util.Map;

@Path("/emails")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmailController {

    @Inject
    MailtrapService mailtrapService;

    @Inject
    TemplateService templateService;  // Inject TemplateService to process templates
    @POST
    @Path("/{templateId}")
    @Transactional
    public Response sendEmail(@PathParam("templateId") Long templateId, EmailRequest emailRequest) {
        // Set the template ID in the email request
        emailRequest.setTemplateId(templateId);
    
        try {
            // Delegate the email sending process to the service
            String result = mailtrapService.sendEmail(emailRequest);
            return Response.ok(result).build();
        } catch (IllegalArgumentException e) {
            // Handle validation or missing template issues
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (IOException e) {
            // Handle email sending issues
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to send email: " + e.getMessage()).build();
        }
    }
    
}
