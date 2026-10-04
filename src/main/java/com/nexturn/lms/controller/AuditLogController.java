package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexturn.lms.entity.AuditLog;
import com.nexturn.lms.service.AuditLogService;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public ResponseEntity<AuditLog> createAuditLog(
            @RequestBody AuditLog auditLog) {

        AuditLog savedAuditLog =
                auditLogService.createAuditLog(auditLog);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAuditLog);
    }

    @GetMapping
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {

        return ResponseEntity.ok(
                auditLogService.getAllAuditLogs());
    }

    @GetMapping("/{logId}")
    public ResponseEntity<AuditLog> getAuditLogById(
            @PathVariable Integer logId) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogById(logId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByUser(
            @PathVariable Integer userId) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByUser(userId));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<AuditLog>> getAuditLogsByApplication(
            @PathVariable Integer applicationId) {

        return ResponseEntity.ok(
                auditLogService.getAuditLogsByApplication(
                        applicationId));
    }
}