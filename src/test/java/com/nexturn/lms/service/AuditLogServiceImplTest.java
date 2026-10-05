package com.nexturn.lms.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.AuditLog;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.AuditLogRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @InjectMocks
    private AuditLogServiceImpl auditLogService;

    private User user;
    private LoanApplication application;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId(1);

        application = new LoanApplication();
        application.setApplicationId(100);
    }

    @Test
    void log_shouldCreateAndSaveAuditLog() {

        auditLogService.log(
                user,
                application,
                "APPLICATION_VERIFIED",
                "Documents verified successfully"
        );

        verify(auditLogRepository)
                .save(any(AuditLog.class));
    }

    @Test
    void log_shouldSaveCorrectAuditLogDetails() {

        auditLogService.log(
                user,
                application,
                "APPLICATION_APPROVED",
                "Loan approved for applicant"
        );

        org.mockito.ArgumentCaptor<AuditLog> captor =
                org.mockito.ArgumentCaptor.forClass(AuditLog.class);

        verify(auditLogRepository)
                .save(captor.capture());

        AuditLog savedLog = captor.getValue();

        assertEquals(
                user,
                savedLog.getUser()
        );

        assertEquals(
                application,
                savedLog.getApplication()
        );

        assertEquals(
                "APPLICATION_APPROVED",
                savedLog.getAction()
        );

        assertEquals(
                "Loan approved for applicant",
                savedLog.getDetails()
        );
    }

    @Test
    void log_shouldSavePaymentAuditLog() {

        auditLogService.log(
                user,
                application,
                "PAYMENT_RECEIVED",
                "EMI payment received successfully"
        );

        org.mockito.ArgumentCaptor<AuditLog> captor =
                org.mockito.ArgumentCaptor.forClass(AuditLog.class);

        verify(auditLogRepository)
                .save(captor.capture());

        AuditLog savedLog = captor.getValue();

        assertEquals(
                "PAYMENT_RECEIVED",
                savedLog.getAction()
        );

        assertEquals(
                "EMI payment received successfully",
                savedLog.getDetails()
        );

        assertEquals(
                user,
                savedLog.getUser()
        );

        assertEquals(
                application,
                savedLog.getApplication()
        );
    }
}