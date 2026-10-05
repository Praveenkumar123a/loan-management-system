package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.Approval;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.ApprovalRepository;
import com.nexturn.lms.repository.LoanApplicationRepository;
import com.nexturn.lms.utils.ApplicationStatus;

@ExtendWith(MockitoExtension.class)
class ApprovalServiceImplTest {

    @Mock
    private ApprovalRepository approvalRepository;

    @Mock
    private LoanApplicationRepository applicationRepository;

    @InjectMocks
    private ApprovalServiceImpl approvalService;

    private LoanApplication application;
    private User manager;

    @BeforeEach
    void setUp() {

        application = new LoanApplication();
        application.setApplicationId(1);
        application.setStatus(ApplicationStatus.RECOMMENDED);

        manager = new User();
        manager.setUserId(3);
    }

    @Test
    void decide_shouldApproveApplication() {

        BigDecimal approvedAmount =
                new BigDecimal("100000");

        BigDecimal interestRate =
                new BigDecimal("8.50");

        Integer tenureMonths = 24;

        when(approvalRepository.save(any(Approval.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(applicationRepository.save(application))
                .thenReturn(application);

        Approval result =
                approvalService.decide(
                        application,
                        manager,
                        true,
                        "Application approved",
                        approvedAmount,
                        interestRate,
                        tenureMonths
                );

        assertNotNull(result);

        assertEquals(
                application,
                result.getApplication()
        );

        assertEquals(
                manager,
                result.getManager()
        );

        assertEquals(
                "APPROVED",
                result.getDecision()
        );

        assertEquals(
                "Application approved",
                result.getComments()
        );

        assertEquals(
                approvedAmount,
                result.getApprovedAmount()
        );

        assertEquals(
                interestRate,
                result.getInterestRate()
        );

        assertEquals(
                tenureMonths,
                result.getTenureMonths()
        );

        assertEquals(
                ApplicationStatus.APPROVED,
                application.getStatus()
        );

        verify(approvalRepository)
                .save(any(Approval.class));

        verify(applicationRepository)
                .save(application);
    }

    @Test
    void decide_shouldRejectApplication() {

        BigDecimal approvedAmount =
                new BigDecimal("100000");

        BigDecimal interestRate =
                new BigDecimal("8.50");

        Integer tenureMonths = 24;

        when(approvalRepository.save(any(Approval.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(applicationRepository.save(application))
                .thenReturn(application);

        Approval result =
                approvalService.decide(
                        application,
                        manager,
                        false,
                        "Application rejected",
                        approvedAmount,
                        interestRate,
                        tenureMonths
                );

        assertNotNull(result);

        assertEquals(
                application,
                result.getApplication()
        );

        assertEquals(
                manager,
                result.getManager()
        );

        assertEquals(
                "REJECTED",
                result.getDecision()
        );

        assertEquals(
                "Application rejected",
                result.getComments()
        );

        // Approved terms must not be set when rejected
        assertNull(result.getApprovedAmount());
        assertNull(result.getInterestRate());
        assertNull(result.getTenureMonths());

        assertEquals(
                ApplicationStatus.REJECTED,
                application.getStatus()
        );

        verify(approvalRepository)
                .save(any(Approval.class));

        verify(applicationRepository)
                .save(application);
    }

    @Test
    void getByApplication_shouldReturnApproval() {

        Approval approval = new Approval();

        approval.setApplication(application);
        approval.setManager(manager);
        approval.setDecision("APPROVED");
        approval.setComments("Approved");

        approval.setApprovedAmount(
                new BigDecimal("100000")
        );

        approval.setInterestRate(
                new BigDecimal("8.50")
        );

        approval.setTenureMonths(24);

        when(approvalRepository
                .findByApplication(application))
                .thenReturn(Optional.of(approval));

        Approval result =
                approvalService.getByApplication(
                        application
                );

        assertNotNull(result);

        assertEquals(
                approval,
                result
        );

        assertEquals(
                "APPROVED",
                result.getDecision()
        );

        assertEquals(
                new BigDecimal("100000"),
                result.getApprovedAmount()
        );

        verify(approvalRepository)
                .findByApplication(application);
    }

    @Test
    void getByApplication_shouldThrowExceptionWhenNotFound() {

        when(approvalRepository
                .findByApplication(application))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> approvalService
                        .getByApplication(application)
        );

        verify(approvalRepository)
                .findByApplication(application);
    }
}