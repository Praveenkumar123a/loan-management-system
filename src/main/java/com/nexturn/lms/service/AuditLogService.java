package com.nexturn.lms.service;

import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;

public interface AuditLogService {
    void log(User user, LoanApplication application, String action, String details);
}