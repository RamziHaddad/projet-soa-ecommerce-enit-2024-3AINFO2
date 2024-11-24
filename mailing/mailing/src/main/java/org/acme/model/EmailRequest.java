package org.acme.model;

import java.util.Map;

public class EmailRequest {

    private String recipient; // Email recipient
    private String subject;   // Subject of the email
    private String templateId; // ID to select the email template
    private Map<String, String> templateParams; // Dynamic content for placeholders

    // Constructor
    public EmailRequest(String recipient, String subject, String templateId, Map<String, String> templateParams) {
        this.recipient = recipient;
        this.subject = subject;
        this.templateId = templateId;
        this.templateParams = templateParams;
    }

    // Getters and Setters
    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, String> getTemplateParams() {
        return templateParams;
    }

    public void setTemplateParams(Map<String, String> templateParams) {
        this.templateParams = templateParams;
    }
}
