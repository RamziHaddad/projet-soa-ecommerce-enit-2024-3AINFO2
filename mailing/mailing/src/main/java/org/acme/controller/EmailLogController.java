package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.model.EmailLog;
import org.acme.service.EmailLogService;

import java.util.List;

@Path("/email-logs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmailLogController {

    @Inject
    EmailLogService emailLogService;

    @POST
    public Response createEmailLog(EmailLog emailLog) {
        emailLogService.saveEmailLog(
                emailLog.recipient,
                emailLog.subject,
                emailLog.content,
                emailLog.status,
                emailLog.mailtrapResponse
        );
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/{id}")
    public Response getEmailLogById(@PathParam("id") Long id) {
        EmailLog emailLog = emailLogService.getEmailLogById(id);
        if (emailLog == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(emailLog).build();
    }

    @GET
    public List<EmailLog> getAllEmailLogs() {
        return emailLogService.getAllEmailLogs();
    }

    @GET
    @Path("/recipient/{email}")
    public List<EmailLog> getLogsByRecipient(@PathParam("email") String recipient) {
        return emailLogService.getLogsByRecipient(recipient);
    }
}
