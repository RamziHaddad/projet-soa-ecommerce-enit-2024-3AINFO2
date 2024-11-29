package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.service.MailtrapService;
import org.acme.model.EmailRequest;
import org.acme.model.Template;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MailResource {

    @Inject
    MailtrapService mailtrapService;

    // Endpoint to send an email using a template
    @POST
    @Path("/emails")
    public Response sendEmail(EmailRequest emailRequest) {
        try {
            String response = mailtrapService.sendEmail(emailRequest);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to send email: " + e.getMessage()).build();
        }
    }

    // Endpoint to create a new email template

}
