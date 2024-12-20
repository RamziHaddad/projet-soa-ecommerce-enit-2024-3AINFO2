package org.acme.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.model.EmailLog;

import java.util.List;

@ApplicationScoped
public class EmailLogRepository implements PanacheRepository<EmailLog> {

    // Create a new EmailLog entry
    public void createEmailLog(EmailLog emailLog) {
        persist(emailLog);
    }

    // Find a single EmailLog by ID
    public EmailLog findById(Long id) {
        return find("id", id).firstResult();
    }

    // Get all EmailLogs
    public List<EmailLog> findAllLogs() {
        return listAll();
    }

    // Find EmailLogs by recipient email
    public List<EmailLog> findByRecipient(String recipient) {
        return list("recipient", recipient);
    }
}
