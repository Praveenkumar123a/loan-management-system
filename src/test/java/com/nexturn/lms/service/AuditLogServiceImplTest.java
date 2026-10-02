package com.nexturn.lms.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.AuditLog;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.AuditLogRepository;
import com.nexturn.lms.repository.LoanApplicationRepository;
import com.nexturn.lms.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuditLogServiceImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @InjectMocks
    private AuditLogServiceImpl auditLogService;

    @Test
    void testCreateAuditLog() {

        User user = new User();

        AuditLog auditLog = new AuditLog();
        auditLog.setUser(user);
        auditLog.setAction("PAYMENT_RECEIVED");
        auditLog.setDetails("Payment received successfully");

        when(auditLogRepository.save(any(AuditLog.class)))
                .thenReturn(auditLog);

        auditLogService.createAuditLog(auditLog);

        verify(auditLogRepository, times(1))
                .save(auditLog);
    }

    @Test
    void testCreateAuditLogWithNull() {

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> auditLogService.createAuditLog(null));

        org.junit.jupiter.api.Assertions.assertEquals(
                "Audit log cannot be null",
                exception.getMessage());

        verify(auditLogRepository, never())
                .save(any(AuditLog.class));
    }

    @Test
    void testCreateAuditLogWithoutUser() {

        AuditLog auditLog = new AuditLog();
        auditLog.setAction("PAYMENT_RECEIVED");

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> auditLogService.createAuditLog(auditLog));

        org.junit.jupiter.api.Assertions.assertEquals(
                "User is required",
                exception.getMessage());

        verify(auditLogRepository, never())
                .save(any(AuditLog.class));
    }

    @Test
    void testCreateAuditLogWithoutAction() {

        AuditLog auditLog = new AuditLog();

        User user = new User();
        auditLog.setUser(user);

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> auditLogService.createAuditLog(auditLog));

        org.junit.jupiter.api.Assertions.assertEquals(
        "Audit action is required",
        exception.getMessage()
);

        verify(auditLogRepository, never())
                .save(any(AuditLog.class));
    }

    @Test
void testGetAuditLogById() {

    AuditLog auditLog = new AuditLog();
    auditLog.setAction("PAYMENT_RECEIVED");

    when(auditLogRepository.findById(1))
            .thenReturn(java.util.Optional.of(auditLog));

    AuditLog result = auditLogService.getAuditLogById(1);

    org.junit.jupiter.api.Assertions.assertSame(auditLog, result);

    verify(auditLogRepository, times(1))
            .findById(1);
}

@Test
void testGetAuditLogByIdNotFound() {

    when(auditLogRepository.findById(1))
            .thenReturn(java.util.Optional.empty());

    RuntimeException exception =
            org.junit.jupiter.api.Assertions.assertThrows(
                    RuntimeException.class,
                    () -> auditLogService.getAuditLogById(1)
            );

    org.junit.jupiter.api.Assertions.assertEquals(
            "Audit log not found",
            exception.getMessage()
    );

    verify(auditLogRepository, times(1))
            .findById(1);
}

@Test
void testGetAuditLogsByUser() {

    User user = new User();

    AuditLog auditLog = new AuditLog();
    auditLog.setUser(user);
    auditLog.setAction("PAYMENT_RECEIVED");

    java.util.List<AuditLog> logs =
            java.util.List.of(auditLog);

    when(userRepository.findById(1))
            .thenReturn(java.util.Optional.of(user));

    when(auditLogRepository.findByUser(user))
            .thenReturn(logs);

    java.util.List<AuditLog> result =
            auditLogService.getAuditLogsByUser(1);

    org.junit.jupiter.api.Assertions.assertEquals(logs, result);

    verify(userRepository, times(1))
            .findById(1);

    verify(auditLogRepository, times(1))
            .findByUser(user);
}

@Test
void testGetAuditLogsByUserUserNotFound() {

    when(userRepository.findById(1))
            .thenReturn(java.util.Optional.empty());

    RuntimeException exception =
            org.junit.jupiter.api.Assertions.assertThrows(
                    RuntimeException.class,
                    () -> auditLogService.getAuditLogsByUser(1)
            );

    org.junit.jupiter.api.Assertions.assertEquals(
            "User not found",
            exception.getMessage()
    );

    verify(userRepository, times(1))
            .findById(1);

    verify(auditLogRepository, never())
            .findByUser(any(User.class));
}
@Test
void testGetAuditLogsByApplication() {

    LoanApplication application = new LoanApplication();

    AuditLog auditLog = new AuditLog();
    auditLog.setApplication(application);
    auditLog.setAction("PAYMENT_RECEIVED");

    java.util.List<AuditLog> logs =
            java.util.List.of(auditLog);

    when(loanApplicationRepository.findById(1))
            .thenReturn(java.util.Optional.of(application));

    when(auditLogRepository.findByApplication(application))
            .thenReturn(logs);

    java.util.List<AuditLog> result =
            auditLogService.getAuditLogsByApplication(1);

    org.junit.jupiter.api.Assertions.assertEquals(logs, result);

    verify(loanApplicationRepository, times(1))
            .findById(1);

    verify(auditLogRepository, times(1))
            .findByApplication(application);
}

@Test
void testGetAuditLogsByApplicationNotFound() {

    when(loanApplicationRepository.findById(1))
            .thenReturn(java.util.Optional.empty());

    RuntimeException exception =
            org.junit.jupiter.api.Assertions.assertThrows(
                    RuntimeException.class,
                    () -> auditLogService.getAuditLogsByApplication(1)
            );

    org.junit.jupiter.api.Assertions.assertEquals(
            "Loan application not found",
            exception.getMessage()
    );

    verify(loanApplicationRepository, times(1))
            .findById(1);

    verify(auditLogRepository, never())
            .findByApplication(any(LoanApplication.class));
}

@Test
void testGetAllAuditLogs() {

    AuditLog auditLog1 = new AuditLog();
    auditLog1.setAction("PAYMENT_RECEIVED");

    AuditLog auditLog2 = new AuditLog();
    auditLog2.setAction("LOAN_APPROVED");

    java.util.List<AuditLog> logs =
            java.util.List.of(auditLog1, auditLog2);

    when(auditLogRepository.findAll())
            .thenReturn(logs);

    java.util.List<AuditLog> result =
            auditLogService.getAllAuditLogs();

    org.junit.jupiter.api.Assertions.assertEquals(logs, result);

    verify(auditLogRepository, times(1))
            .findAll();
}
}