package com.nexturn.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.AuditLog;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.AuditLogNotFoundException;
import com.nexturn.lms.exception.InvalidAuditLogException;
import com.nexturn.lms.exception.LoanApplicationNotFoundException;
import com.nexturn.lms.exception.UserNotFoundException;
import com.nexturn.lms.repository.AuditLogRepository;
import com.nexturn.lms.repository.LoanApplicationRepository;
import com.nexturn.lms.repository.UserRepository;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    public AuditLogServiceImpl(
            AuditLogRepository auditLogRepository,
            UserRepository userRepository,
            LoanApplicationRepository loanApplicationRepository) {

        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    @Override
    public AuditLog createAuditLog(AuditLog auditLog) {

        if (auditLog == null) {
            throw new InvalidAuditLogException(
                    "Audit log cannot be null");
        }

        if (auditLog.getUser() == null) {
            throw new InvalidAuditLogException(
                    "User is required");
        }

        if (auditLog.getAction() == null ||
                auditLog.getAction().isBlank()) {

            throw new InvalidAuditLogException(
                    "Audit action is required");
        }

        return auditLogRepository.save(auditLog);
    }

    @Override
    public AuditLog getAuditLogById(Integer logId) {

        return auditLogRepository.findById(logId)
                .orElseThrow(() ->
                        new AuditLogNotFoundException(
                                "Audit log not found"));
    }

    @Override
    public List<AuditLog> getAuditLogsByUser(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found"));

        return auditLogRepository.findByUser(user);
    }

    @Override
    public List<AuditLog> getAuditLogsByApplication(
            Integer applicationId) {

        LoanApplication application =
                loanApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new LoanApplicationNotFoundException(
                                        "Loan application not found"));

        return auditLogRepository.findByApplication(application);
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {

        return auditLogRepository.findAll();
    }
}