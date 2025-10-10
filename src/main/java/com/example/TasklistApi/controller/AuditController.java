package com.example.TasklistApi.controller;

import com.example.TasklistApi.model.AuditLog;
import com.example.TasklistApi.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin(origins = "*")
@Tag(name = "Audit Logs", description = "Audit trail operations")
public class AuditController {
    
    @Autowired
    private AuditService auditService;

    @Operation(summary = "Get all audit logs")
    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(auditService.getAllAuditLogs());
    }

    @Operation(summary = "Get audit logs by username")
    @GetMapping("/user/{username}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByUser(
            @Parameter(description = "Username") @PathVariable String username) {
        return ResponseEntity.ok(auditService.getAuditLogsByUser(username));
    }

    @Operation(summary = "Get audit logs for a specific task")
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsForTask(
            @Parameter(description = "Task ID") @PathVariable Long taskId) {
        return ResponseEntity.ok(auditService.getAuditLogsForEntity("TASK", taskId));
    }

    @Operation(summary = "Get audit logs by action type")
    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByAction(
            @Parameter(description = "Action type (CREATE, UPDATE, DELETE, COMPLETE)") 
            @PathVariable String action) {
        return ResponseEntity.ok(auditService.getAuditLogsByAction(action));
    }

    @Operation(summary = "Get audit logs within date range")
    @GetMapping("/date-range")
    public ResponseEntity<List<AuditLog>> getAuditLogsByDateRange(
            @Parameter(description = "Start date") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "End date") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(auditService.getAuditLogsByDateRange(start, end));
    }
}