package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.acme.service.MailtrapService;

import java.io.IOException;

@Path("/mail")
public class MailResource {

    @Inject
    MailtrapService mailtrapService;

    @GET
    @Path("/send")
    public String sendTestEmail() {
        String toEmail = "samsoum.tecktonick@gmail.com";
        String subject = "You are awesome!";
        String message = "Congrats for sending a test email with Mailtrap!";
        try {
            return mailtrapService.sendEmail(subject, message, toEmail);
        } catch (IOException e) {
            e.printStackTrace(); // Log the error or handle it as needed
            return "Error sending email: " + e.getMessage();
        }
    }
}