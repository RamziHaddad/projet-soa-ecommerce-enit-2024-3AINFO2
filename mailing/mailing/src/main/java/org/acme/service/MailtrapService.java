package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.client.MailTrapClient;
import org.acme.model.EmailRequest;
//import org.acme.model.Template;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
//import java.util.Map;

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
    private final TemplateService templateService;
    public MailtrapService(MailTrapClient mailtrapClient, TemplateService templateService) {
        this.mailtrapClient = mailtrapClient;
        this.templateService = templateService;
    }
   

    public String sendEmail(EmailRequest emailRequest) throws IOException {
        // Retrieve the template body using the template ID
        String templateBody = templateService.processTemplate(emailRequest.getTemplateId(), emailRequest.getTemplateParams());
    
        // Retry logic for sending email
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                // Use the Mailtrap client to send the email
                return mailtrapClient.sendEmail(emailRequest.getSubject(), templateBody, emailRequest.getRecipient());
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
    

   /* public String createTemplate(Template Template) {
        // Logic for creating a new email template
        return null;
    }*/

    public String getTemplate(String templateId) {
        // Logic for fetching a template by ID
        return null;
    }
}
