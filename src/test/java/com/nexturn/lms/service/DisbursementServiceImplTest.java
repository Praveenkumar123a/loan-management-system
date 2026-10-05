package com.nexturn.lms.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.Approval;
import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.DisbursementRepository;
import com.nexturn.lms.repository.EmiScheduleRepository;
import com.nexturn.lms.repository.LoanApplicationRepository;
import com.nexturn.lms.utils.ApplicationStatus;

@ExtendWith(MockitoExtension.class)
class DisbursementServiceImplTest {

    @Mock
    private DisbursementRepository disbursementRepository;

    @Mock
    private EmiScheduleRepository emiScheduleRepository;

    @Mock
    private LoanApplicationRepository applicationRepository;

    @Mock
    private ApprovalService approvalService;

    @InjectMocks
    private DisbursementServiceImpl disbursementService;

    private LoanApplication application;
    private User user;
    private Approval approval;
    private Disbursement disbursement;

    @BeforeEach
    void setUp() {

        application = new LoanApplication();
        application.setApplicationId(1);

        user = new User();
        user.setUserId(2);

        approval = new Approval();
        approval.setApprovedAmount(new BigDecimal("500000"));
        approval.setInterestRate(new BigDecimal("10"));
        approval.setTenureMonths(12);

        disbursement = new Disbursement();
        disbursement.setDisbursementId(1);
        disbursement.setApplication(application);
        disbursement.setDisbursedAmount(new BigDecimal("500000"));
    }

    @Test
    void disburse_shouldCreateDisbursementAndEmiSchedule() {

        LocalDate disbursedDate = LocalDate.of(2026, 10, 1);

        when(approvalService.getByApplication(application))
                .thenReturn(approval);

        when(disbursementRepository.save(any(Disbursement.class)))
                .thenReturn(disbursement);

        when(emiScheduleRepository.save(any(EmiSchedule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(applicationRepository.save(application))
                .thenReturn(application);

        Disbursement result = disbursementService.disburse(
                application,
                user,
                disbursedDate
        );

        // Result
        org.junit.jupiter.api.Assertions.assertNotNull(result);

        // Approval lookup
        verify(approvalService)
                .getByApplication(application);

        // Disbursement saved
        verify(disbursementRepository)
                .save(any(Disbursement.class));

        // 12 EMI schedules should be generated
        verify(emiScheduleRepository, times(12))
                .save(any(EmiSchedule.class));

        // Application status updated
        org.junit.jupiter.api.Assertions.assertEquals(
                ApplicationStatus.DISBURSED,
                application.getStatus()
        );

        // Application saved
        verify(applicationRepository)
                .save(application);
    }
}