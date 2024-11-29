package org.acme.service;

import org.acme.model.Template;

import java.util.List;
import java.util.Map;

public class TemplateService {
    public String processTemplate(String templateName, Map<String, String> templateData) {
        return "1";
    }

    public Template getTemplateById(int templateId) {
        // Simulate fetching a template from the database
        // Replace this with your actual database retrieval logic
        Template template = new Template();
        template.setId(templateId);
        template.setContent("Dear Customer, your price is {price} and the arrival date is {arrivalDate}.");
        return template;
    }


    public void validateParams(List<String> requiredPlaceholders, Map<String, String> providedParams) {
        for (String placeholder : requiredPlaceholders) {
            if (!providedParams.containsKey(placeholder)) {
                throw new IllegalArgumentException("Missing parameter: " + placeholder);
            }
        }
    }

    public String createTemplate(Template template) {
    }
}