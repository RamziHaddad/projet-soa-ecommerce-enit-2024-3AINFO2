package org.acme.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
//import org.acme.model.Template;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

@ApplicationScoped
public class MailTrapClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailTrapClient.class);

    @ConfigProperty(name = "mailtrap.api.base-url")
    String apiBaseUrl;

    @ConfigProperty(name = "mailtrap.api.token")
    String apiToken;

    private final Client client = ClientBuilder.newClient();

    /**
     * Fetch a template by its ID from the Mailtrap API.
     * 
     * @param templateId The ID of the template to fetch.
     * @return The template content as a String.
     * @throws IOException If the template cannot be fetched.
     */
    public String getTemplateById(String templateId) throws IOException {
        String url = String.format("%s/templates/%s", apiBaseUrl, templateId);

        Response response = client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiToken)
                .get();

        if (response.getStatus() == 200) {
            return response.readEntity(String.class);
        } else {
            LOGGER.error("Failed to fetch template. Status: {}, Error: {}",
                    response.getStatus(),
                    response.readEntity(String.class));
            throw new IOException("Failed to fetch template with ID: " + templateId);
        }
    }

    /**
     * Send an email using the Mailtrap API.
     * 
     * @param subject The subject of the email.
     * @param body The body/content of the email.
     * @param recipient The recipient's email address.
     * @return The Mailtrap API response as a String.
     * @throws IOException If the email cannot be sent.
     */
    public String sendEmail(String subject, String body, String recipient) throws IOException {
        String url = String.format("%s/send", apiBaseUrl);

        // Construct email payload
        Map<String, Object> payload = Map.of(
                "from", Map.of(
                        "email", "your-email@example.com",
                        "name", "Your Service"
                ),
                "to", new Map[]{
                        Map.of("email", recipient)
                },
                "subject", subject,
                "html", body
        );

        Response response = client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + apiToken)
                .post(Entity.entity(payload, MediaType.APPLICATION_JSON));

        if (response.getStatus() == 200 || response.getStatus() == 202) {
            return response.readEntity(String.class);
        } else {
            LOGGER.error("Failed to send email. Status: {}, Error: {}",
                    response.getStatus(),
                    response.readEntity(String.class));
            throw new IOException("Failed to send email to: " + recipient);
        }
    }

    /**
     * Clean up resources.
     */
    public void close() {
        client.close();
    }
}
