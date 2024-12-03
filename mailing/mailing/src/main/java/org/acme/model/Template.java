package org.acme.model;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "templates")
public class Template extends PanacheEntity {

    @Column(nullable = false, unique = true)
    public String templateId; // Unique template identifier

    @Column(nullable = false, length = 2000)
    public String content; // Template content with placeholders

    @ElementCollection
    @CollectionTable(name = "template_params", joinColumns = @JoinColumn(name = "template_id"))
    @MapKeyColumn(name = "param_name")
    @Column(name = "param_value")
    public Map<String, String> templateParams; // Variables for placeholders

    // Getters and Setters
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getTemplateParams() {
        return templateParams;
    }

    public void setTemplateParams(Map<String, String> templateParams) {
        this.templateParams = templateParams;
    }
}
