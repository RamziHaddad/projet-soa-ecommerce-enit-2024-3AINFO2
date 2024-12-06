package org.acme.repository;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.acme.model.Template;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class TemplateRepository implements PanacheRepository<Template> {

    /**
     * Find a template by its unique template ID.
     *
     * @param templateId The unique template ID to find.
     * @return An Optional containing the Template if found, otherwise empty.
     */
    public Optional<Template> findByTemplateId(String templateId) {
        return find("templateId", templateId).firstResultOptional();
    }

    /**
     * Retrieve all templates from the database.
     *
     * @return A list of all templates.
     */
    public List<Template> findAllTemplates() {
        return listAll();
    }

    /**
     * Save or update a template in the database.
     *
     * @param template The template to persist or update.
     */
    public void saveOrUpdate(Template template) {
        if (template.getTemplateId() != null) {
            Template existingTemplate = findById(Long.parseLong(template.getTemplateId()));
            if (existingTemplate != null) {
                // Update existing template
                existingTemplate.setContent(template.getContent());
                persist(existingTemplate);
                return;
            }
        }
        // Save new template
        persist(template);
    }

    /**
     * Delete a template by its unique template ID.
     *
     * @param templateId The unique template ID to delete.
     */
    public void deleteByTemplateId(String templateId) {
        delete("templateId", templateId);
    }
}
