package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.AuditLog;

public interface AuditLogService {

    AuditLog createAuditLog(AuditLog auditLog);

    AuditLog getAuditLogById(Integer logId);

    List<AuditLog> getAuditLogsByUser(Integer userId);

    List<AuditLog> getAuditLogsByApplication(Integer applicationId);

    List<AuditLog> getAllAuditLogs();
}