package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.client.MailTrapClient;
import org.acme.model.EmailRequest;
import org.acme.model.TemplateRequest;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

@ApplicationScoped
public class MailtrapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailtrapService.class);
    private static final int MAX_RETRIES = 3;

    @ConfigProperty(name = "mailtrap.api.base-url", defaultValue = "")
    String apiBaseUrl;

    @ConfigProperty(name = "mailtrap.api.token", defaultValue = "")
    String apiToken;

    @ConfigProperty(name = "mailtrap.from.email", defaultValue = "")
    String fromEmail;

    @ConfigProperty(name = "mailtrap.from.name", defaultValue = "Your Name")
    String fromName;

    private final MailTrapClient mailtrapClient;

    public MailtrapService(MailTrapClient mailtrapClient) {
        this.mailtrapClient = mailtrapClient;
    }

    public String sendEmail(EmailRequest emailRequest) throws IOException {
        // Retrieve the template by ID
        String templateBody = mailtrapClient.getTemplateById(emailRequest.getTemplateId());

        // Replace placeholders with dynamic content
        String body = replacePlaceholders(templateBody, emailRequest.getTemplateParams());

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                // Send the email using the Mailtrap API
                return mailtrapClient.sendEmail(emailRequest.getSubject(), body, emailRequest.getRecipient());
            } catch (IOException e) {
                if (attempt == MAX_RETRIES) {
                    LOGGER.error("All retry attempts failed. Unable to send email.", e);
                    throw e; // Rethrow after max retries
                }
                LOGGER.warn("Attempt {} failed. Retrying...", attempt, e);
            }
        }
        throw new IllegalStateException("Email sending retries exceeded.");
    }

    private String replacePlaceholders(String templateBody, Map<String, String> templateParams) {
        // Replace placeholders with actual dynamic content
        for (Map.Entry<String, String> entry : templateParams.entrySet()) {
            templateBody = templateBody.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return templateBody;
    }

    public String createTemplate(TemplateRequest templateRequest) {
        // Logic for creating a new email template
        return null;
    }

    public String getTemplate(String templateId) {
        // Logic for fetching a template by ID
        return null;
    }
}
