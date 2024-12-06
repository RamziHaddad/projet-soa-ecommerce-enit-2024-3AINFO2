package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.*;
//import org.acme.client.MailTrapClient;
//import org.acme.model.EmailRequest;
//import org.acme.model.Template;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
//import java.util.Map;
@ApplicationScoped
public class MailtrapService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailtrapService.class);

    @ConfigProperty(name = "mailtrap.api.base-url", defaultValue = "")
    String apiBaseUrl;

    @ConfigProperty(name = "mailtrap.api.token", defaultValue = "")
    String apiToken;

    @ConfigProperty(name = "mailtrap.from.email", defaultValue = "")
    String fromEmail;

    @ConfigProperty(name = "mailtrap.from.name", defaultValue = "Your Name")
    String fromName;

    //private final MailTrapClient mailtrapClient;

    // public MailtrapService(MailTrapClient mailtrapClient) {
    //     this.mailtrapClient = mailtrapClient;
    // }
    private final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();


            
    public String sendEmail(String subject, String content, String recipient) throws IOException {
        LOGGER.info("Sending email - Subject: {}, Recipient: {}", subject, recipient);
        LOGGER.debug("Email content: {}", content);
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
