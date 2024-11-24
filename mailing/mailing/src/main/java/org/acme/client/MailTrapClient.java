package org.acme.client;

import okhttp3.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;

@ApplicationScoped
public class MailTrapClient {

    @ConfigProperty(name = "mailtrap.api.base-url")
    String apiBaseUrl;

    @ConfigProperty(name = "mailtrap.api.token")
    String apiToken;

    @ConfigProperty(name = "mailtrap.from.email")
    String fromEmail;

    private final OkHttpClient client = new OkHttpClient.Builder().build();

    // Get an email template by ID
    public String getTemplateById(String templateId) throws IOException {
        // In a real-world scenario, you'd fetch the template from Mailtrap or your database
        // Here we'll just simulate returning a basic template string
        return "Dear Customer, your price is {price} and the arrival date is {arrivalDate}.";
    }

    // Send an email through Mailtrap API
    public String sendEmail(String subject, String body, String recipient) throws IOException {
        String jsonPayload = String.format(
                "{\"from\":{\"email\":\"%s\",\"name\":\"Your Name\"},\"to\":[{\"email\":\"%s\"}],\"subject\":\"%s\",\"text\":\"%s\"}",
                fromEmail, recipient, subject, body
        );

        RequestBody requestBody = RequestBody.create(jsonPayload, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(apiBaseUrl + "/send")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiToken)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Failed to send email: " + response.code());
            }
        }
    }
}
