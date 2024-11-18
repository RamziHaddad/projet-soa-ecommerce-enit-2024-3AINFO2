package org.acme;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;

@ApplicationScoped
public class MailtrapService {

    @ConfigProperty(name = "mailtrap.api.base-url")
    String apiBaseUrl;

    @ConfigProperty(name = "mailtrap.api.token")
    String apiToken;

    @ConfigProperty(name = "mailtrap.from.email")
    String fromEmail;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();

    public String sendEmail(String subject, String body, String recipient) throws IOException {
        if (apiBaseUrl == null || apiBaseUrl.isEmpty()) {
            throw new IllegalStateException("API Base URL is not configured");
        }
        if (apiToken == null || apiToken.isEmpty()) {
            throw new IllegalStateException("API Token is not configured");
        }

        // Create the JSON payload
        String jsonPayload = String.format(
                "{\"from\":{\"email\":\"%s\",\"name\":\"Your Name\"},\"to\":[{\"email\":\"%s\"}],\"subject\":\"%s\",\"text\":\"%s\"}",
                fromEmail, recipient, subject, body
        );
        System.out.println("Sending JSON payload: " + jsonPayload);  // Log the payload

        // Build the request
        RequestBody requestBody = RequestBody.create(
                jsonPayload, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(apiBaseUrl)
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiToken)
                .addHeader("Content-Type", "application/json")
                .build();

        // Execute the request and get the response
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                String responseBody = response.body() != null ? response.body().string() : "No response body";
                throw new IOException(String.format("Request failed with code: %d, body: %s",
                        response.code(), responseBody));
            }
        }
    }
}
