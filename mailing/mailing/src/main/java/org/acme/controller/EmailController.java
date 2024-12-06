package org.acme.controller;

import org.acme.model.EmailRequest;
import org.acme.service.MailtrapService;
import org.acme.service.TemplateService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;
import java.util.Map;

@Path("/emails")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmailController {

    @Inject
    MailtrapService mailtrapService;

    @Inject
    TemplateService templateService;

    @POST
    @Path("/{templateId}")
    @Transactional
    public Response sendEmail(@PathParam("templateId") Long templateId, EmailRequest emailRequest) {
        emailRequest.setTemplateId(templateId); // Ensure the template ID is set in the request

        try {
            // Step 1: Generate the email content using TemplateService
            String processedContent = templateService.processTemplate(templateId, emailRequest.getTemplateParams());

            // Step 2: Use MailtrapService to send the email
           String result = mailtrapService.sendEmail(emailRequest.getSubject(), processedContent, emailRequest.getRecipient());

            // Step 3: Return the processed content and result in the response
            return Response.ok(Map.of(
                    "processedContent", processedContent,
                 "result", result
            )).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to send email: " + e.getMessage()).build();
        }
    }
}
