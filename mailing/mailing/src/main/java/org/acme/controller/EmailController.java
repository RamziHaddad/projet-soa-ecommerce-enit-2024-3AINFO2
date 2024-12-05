
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
import java.util.HashMap;
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
        emailRequest.setTemplateId(templateId);

        try {
            // Generate the email content
            String processedContent = templateService.processTemplate(templateId, emailRequest.getTemplateParams());

            // Send the email and get the result from MailtrapService
            String mailtrapResult = mailtrapService.sendEmail(emailRequest);

            // Return the result along with the processed email content
            Map<String, Object> responsePayload = new HashMap<>();
            responsePayload.put("mailtrapResult", mailtrapResult);
            responsePayload.put("processedContent", processedContent);

            return Response.ok(responsePayload).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (IOException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to send email: " + e.getMessage()).build();
        }
    }
}

