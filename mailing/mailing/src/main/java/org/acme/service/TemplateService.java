package org.acme.service;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.model.Template;
import org.acme.repository.TemplateRepository;
import java.util.List;
import java.util.Map;
@ApplicationScoped
public class TemplateService {

    @Inject
    TemplateRepository templateRepository;

    /**
     * Processes a template by replacing placeholders with provided values.
     *
     * @param templateId   The ID of the template to process.
     * @param templateData Map of placeholder keys to their replacement values.
     * @return Processed template content.
     */
    public String processTemplate(Long templateId, Map<String, String> templateData) {
        Template template = templateRepository.findById(templateId);
        if (template == null) {
            throw new IllegalArgumentException("Template not found for ID: " + templateId);
        }
        String content = template.getContent();
        for (Map.Entry<String, String> entry : templateData.entrySet()) {
            content = content.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return content;
    }

    /**
     * Retrieves a template by its ID.
     *
     * @param templateId The database ID of the template.
     * @return The corresponding Template object.
     */
    public Template getTemplateById(Long templateId) {
        Template template = templateRepository.findById(templateId);
        if (template == null) {
            throw new IllegalArgumentException("Template not found for ID: " + templateId);
        }
        return template;
    }

    /**
     * Validates that all required placeholders have corresponding values.
     *
     * @param requiredPlaceholders List of required placeholder keys.
     * @param providedParams       Map of provided placeholder keys to values.
     */
    public void validateParams(List<String> requiredPlaceholders, Map<String, String> providedParams) {
        for (String placeholder : requiredPlaceholders) {
            if (!providedParams.containsKey(placeholder)) {
                throw new IllegalArgumentException("Missing parameter: " + placeholder);
            }
        }
    }

    /**
     * Creates a new template in the database.
     *
     * @param template The Template object to create.
     * @return The persisted Template object.
     */
    public Template createTemplate(Template template) {
        templateRepository.persist(template);
        return template;
    }

    /**
     * Retrieves all templates from the database.
     *
     * @return List of all Template objects.
     */
    public List<Template> getAllTemplates() {
        return templateRepository.listAll();
    }

    /**
     * Updates an existing template in the database.
     *
     * @param id             The ID of the template to update.
     * @param updatedTemplate The updated Template object.
     * @return The updated Template object.
     */
    public Template updateTemplate(Long id, Template updatedTemplate) {
        Template existingTemplate = templateRepository.findById(id);
        if (existingTemplate == null) {
            throw new IllegalArgumentException("Template not found for ID: " + id);
        }
        existingTemplate.setContent(updatedTemplate.getContent());
        existingTemplate.setTemplateParams(updatedTemplate.getTemplateParams());
        return existingTemplate;
    }

    /**
     * Deletes a template by its ID.
     *
     * @param id The ID of the template to delete.
     * @return True if the template was successfully deleted, false otherwise.
     */
    public boolean deleteTemplate(Long id) {
        return templateRepository.deleteById(id);
    }
}
