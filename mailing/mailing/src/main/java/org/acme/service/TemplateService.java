package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.Template;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class TemplateService {
    public String processTemplate(int templateId, Map<String, String> templateData) {
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

    public Template createTemplate() {

        return new Template();
    }

    public List<Template> getAllTemplates() {
        return null;
    }


    public Template updateTemplate(int id, Template updatedTemplate) {
        Template existingTemplate = getTemplateById(id);
        if (existingTemplate == null) {
            throw new IllegalArgumentException("Template not found with id: " + id);
        }
        // Update the existing template with new values
        existingTemplate.setContent(updatedTemplate.getContent());
        // Add logic to save the updated template to the database
        // For example: templateRepository.save(existingTemplate);
        return existingTemplate;
    }
    public boolean deleteTemplate(int id) {
        return false;
    }
}


