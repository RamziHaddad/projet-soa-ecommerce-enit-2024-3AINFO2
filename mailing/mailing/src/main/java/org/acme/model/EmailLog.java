package org.acme.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class EmailLog extends PanacheEntity {

    public String recipient;
    public String subject;
    public String content;
    public String status; // e.g., "SENT" or "FAILED"
    public String mailtrapResponse; // Optional: Store Mailtrap's response for debugging

    // Default constructor (required by JPA)
    public EmailLog() {}

    // Convenience constructor
    public EmailLog(String recipient, String subject, String content, String status, String mailtrapResponse) {
        this.recipient = recipient;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.mailtrapResponse = mailtrapResponse;
    }
}
