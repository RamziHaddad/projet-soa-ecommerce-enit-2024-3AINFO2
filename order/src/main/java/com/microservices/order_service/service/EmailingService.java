package com.microservices.order_service.service;

import com.microservices.order_service.request.EmailRequest;
import org.hibernate.sql.Template;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

@Service
public class EmailingService {

    private final WebClient webClient;

    public EmailingService(@Value("${mail.service.url}") String mailServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(mailServiceUrl)
                .build();
    }

    public String sendEmail(int templateId, EmailRequest emailRequest) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/emails/{templateId}")
                        .build(templateId))
                .bodyValue(emailRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public Template getTemplateById(int templateId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/templates/{templateId}")
                        .build(templateId))
                .retrieve()
                .bodyToMono(Template.class)
                .block();
    }
}
