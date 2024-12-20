package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.*;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@ApplicationScoped
public class MailtrapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailtrapService.class);

    @Inject
    EmailLogService emailLogService;

    @ConfigProperty(name = "mailtrap.api.base-url", defaultValue = "")
    String apiBaseUrl;

    @ConfigProperty(name = "mailtrap.api.token", defaultValue = "")
    String apiToken;

    @ConfigProperty(name = "mailtrap.from.email", defaultValue = "")
    String fromEmail;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    public String sendEmail(String subject, String content, String recipient) throws IOException {
        LOGGER.info("Sending email - Subject: {}, Recipient: {}", subject, recipient);

        if (apiBaseUrl == null || apiBaseUrl.isEmpty()) {
            throw new IllegalStateException("API Base URL is not configured");
        }
        if (apiToken == null || apiToken.isEmpty()) {
            throw new IllegalStateException("API Token is not configured");
        }

        String jsonPayload = String.format(
                "{\"from\":{\"email\":\"%s\",\"name\":\"Your Name\"},\"to\":[{\"email\":\"%s\"}],\"subject\":\"%s\",\"text\":\"%s\"}",
                fromEmail, recipient, subject, content
        );

        RequestBody requestBody = RequestBody.create(jsonPayload, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(apiBaseUrl)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiToken)
                .addHeader("Content-Type", "application/json")
                .build();

        String status = "FAILED";
        String mailtrapResponse = null;

        try (Response response = client.newCall(request).execute()) {
            mailtrapResponse = response.body() != null ? response.body().string() : "No response body";

            if (response.isSuccessful()) {
                status = "SENT";
                return mailtrapResponse;
            } else {
                throw new IOException(String.format("Request failed with code: %d, body: %s",
                        response.code(), mailtrapResponse));
            }
        } catch (IOException e) {
            LOGGER.error("Failed to send email: {}", e.getMessage());
            throw e;
        } finally {
            // Log the email details in the database
            emailLogService.saveEmailLog(recipient, subject, content, status, mailtrapResponse);
        }
    }
}
