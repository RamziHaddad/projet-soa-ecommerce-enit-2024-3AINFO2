//The MailResource class implements the HTTP API exposed by our application. It handles GET request on `http://localhost:8080/mail.
package main.java.org.acme;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.smallrye.common.annotation.Blocking;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/mail")
public class MailResource {

    @Inject
    Mailer mailer;

    @GET
    @Blocking
    @GET
    @Blocking
    public Response sendEmail() {
        mailer.send(
                Mail.withText("quarkus@quarkus.io",
                        "Ahoy from Quarkus",
                        "A simple email sent from a Quarkus application."));
        return Response.ok("Email sent successfully!").build(); // Return a success message
    }

}