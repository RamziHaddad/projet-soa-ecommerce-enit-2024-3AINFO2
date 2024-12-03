package org.acme.model;

import java.util.Map;

public class EmailRequest {

    private String recipient; // The email recipient
    private String subject;   // Subject of the email
    private Long templateId;  // ID of the template to use (changed to Long)
    private Map<String, String> templateParams; // Dynamic values for template placeholders

    // Default Constructor
    public EmailRequest() {
    }

    // Constructor with parameters
    public EmailRequest(String recipient, String subject, Long templateId, Map<String, String> templateParams) {
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

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public Map<String, String> getTemplateParams() {
        return templateParams;
    }

    public void setTemplateParams(Map<String, String> templateParams) {
        this.templateParams = templateParams;
    }

    @Override
    public String toString() {
        return "EmailRequest{" +
                "recipient='" + recipient + '\'' +
                ", subject='" + subject + '\'' +
                ", templateId=" + templateId +
                ", templateParams=" + templateParams +
                '}';
    }
}
