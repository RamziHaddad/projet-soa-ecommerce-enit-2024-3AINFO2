package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.model.EmailLog;
import org.acme.repository.EmailLogRepository;

import java.util.List;

@ApplicationScoped
public class EmailLogService {

    @Inject
    EmailLogRepository emailLogRepository;

    // Create a new log entry
    public void saveEmailLog(String recipient, String subject, String content, String status, String mailtrapResponse) {
        EmailLog emailLog = new EmailLog(recipient, subject, content, status, mailtrapResponse);
        emailLogRepository.createEmailLog(emailLog);
    }

    // Retrieve a single log by ID
    public EmailLog getEmailLogById(Long id) {
        return emailLogRepository.findById(id);
    }

    // Retrieve all logs
    public List<EmailLog> getAllEmailLogs() {
        return emailLogRepository.findAllLogs();
    }

    // Retrieve logs by recipient
    public List<EmailLog> getLogsByRecipient(String recipient) {
        return emailLogRepository.findByRecipient(recipient);
    }
}
