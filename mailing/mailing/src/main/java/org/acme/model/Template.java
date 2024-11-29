package org.acme.model;

import java.util.Map;

public class Template {

    private String id; // Unique identifier for the template
    private String content; // The actual template content with placeholders
    private Map<String, String> templateParams; // Parameters to replace placeholders in the template

    public Template(String id, Map<String, String> templateParams, String content) {
        this.id = id;
        this.templateParams = templateParams;
        this.content = content;
    }

    // Getter for id
    public String getId() {
        return id;
    }

    // Setter for id
    public void setId(String id) {
        this.id = id;
    }

    // Getter for content
    public String getContent() {
        return content;
    }

    // Setter for content
    public void setContent(String content) {
        this.content = content;
    }

    // Getter for templateParams
    public Map<String, String> getTemplateParams() {
        return templateParams;
    }

    // Setter for templateParams
    public void setTemplateParams(Map<String, String> templateParams) {
        this.templateParams = templateParams;
    }
}
