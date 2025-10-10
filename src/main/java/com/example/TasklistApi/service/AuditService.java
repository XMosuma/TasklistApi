package com.example.TasklistApi.service;

import com.example.TasklistApi.model.AuditLog;
import com.example.TasklistApi.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditService.class);
    
    @Autowired
    private AuditLogRepository auditLogRepository;

    public void logAction(String username, String action, String entityType, Long entityId, String details) {
        AuditLog auditLog = new AuditLog(username, action, entityType, entityId, details);
        auditLogRepository.save(auditLog);
        
        logger.info("AUDIT: User '{}' performed '{}' on {} with ID: {} - {}", 
                   username, action, entityType, entityId, details);
    }

    public List<AuditLog> getAuditLogsByUser(String username) {
        return auditLogRepository.findByUsername(username);
    }

    public List<AuditLog> getAuditLogsForEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    public List<AuditLog> getAuditLogsByAction(String action) {
        return auditLogRepository.findByAction(action);
    }

    public List<AuditLog> getAuditLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        return auditLogRepository.findByTimestampBetween(start, end);
    }

    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }
}