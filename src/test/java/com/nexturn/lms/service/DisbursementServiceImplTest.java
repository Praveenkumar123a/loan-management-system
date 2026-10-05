package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
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
import com.nexturn.lms.utils.EmiStatus;

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
    private User manager;
    private Approval approval;
    private LocalDate disbursementDate;

    @BeforeEach
    void setUp() {

        application = new LoanApplication();
        application.setApplicationId(1);
        application.setStatus(ApplicationStatus.APPROVED);

        manager = new User();
        manager.setUserId(3);

        disbursementDate = LocalDate.of(2026, 10, 1);

        approval = new Approval();
        approval.setApplication(application);
        approval.setManager(manager);
        approval.setDecision("APPROVED");
        approval.setApprovedAmount(
                new BigDecimal("100000")
        );
        approval.setInterestRate(
                new BigDecimal("12.00")
        );
        approval.setTenureMonths(12);
    }

    @Test
    void disburse_shouldCreateDisbursementAndGenerateEmiSchedule() {

        when(approvalService.getByApplication(application))
                .thenReturn(approval);

        when(disbursementRepository.save(any(Disbursement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(emiScheduleRepository.save(any(EmiSchedule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(applicationRepository.save(application))
                .thenReturn(application);

        Disbursement result =
                disbursementService.disburse(
                        application,
                        manager,
                        disbursementDate
                );

        assertNotNull(result);

        assertEquals(
                application,
                result.getApplication()
        );

        assertEquals(
                new BigDecimal("100000"),
                result.getDisbursedAmount()
        );

        assertEquals(
                disbursementDate,
                result.getDisbursedDate()
        );

        assertEquals(
                manager,
                result.getDisbursedBy()
        );

        assertEquals(
                ApplicationStatus.DISBURSED,
                application.getStatus()
        );

        verify(approvalService)
                .getByApplication(application);

        verify(disbursementRepository)
                .save(any(Disbursement.class));

        verify(applicationRepository)
                .save(application);

        verify(emiScheduleRepository, times(12))
                .save(any(EmiSchedule.class));
    }

    @Test
    void disburse_shouldGenerateCorrectFirstEmi() {

        when(approvalService.getByApplication(application))
                .thenReturn(approval);

        when(disbursementRepository.save(any(Disbursement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(emiScheduleRepository.save(any(EmiSchedule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(applicationRepository.save(application))
                .thenReturn(application);

        disbursementService.disburse(
                application,
                manager,
                disbursementDate
        );

        ArgumentCaptor<EmiSchedule> captor =
                ArgumentCaptor.forClass(EmiSchedule.class);

        verify(emiScheduleRepository, times(12))
                .save(captor.capture());

        EmiSchedule firstEmi =
                captor.getAllValues().get(0);

        assertEquals(
                1,
                firstEmi.getInstallmentNo()
        );

        assertEquals(
                disbursementDate.plusMonths(1),
                firstEmi.getDueDate()
        );

        assertEquals(
                new BigDecimal("100000"),
                firstEmi.getDisbursement()
                        .getDisbursedAmount()
        );

        assertEquals(
                EmiStatus.PENDING,
                firstEmi.getStatus()
        );

        assertEquals(
                BigDecimal.ZERO,
                firstEmi.getPenaltyAmount()
        );

        assertNotNull(
                firstEmi.getEmiAmount()
        );

        assertNotNull(
                firstEmi.getPrincipalComponent()
        );

        assertNotNull(
                firstEmi.getInterestComponent()
        );

        assertNotNull(
                firstEmi.getOutstandingBalance()
        );
    }

    @Test
    void disburse_shouldGenerateCorrectNumberOfEmis() {

        when(approvalService.getByApplication(application))
                .thenReturn(approval);

        when(disbursementRepository.save(any(Disbursement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(emiScheduleRepository.save(any(EmiSchedule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(applicationRepository.save(application))
                .thenReturn(application);

        disbursementService.disburse(
                application,
                manager,
                disbursementDate
        );

        ArgumentCaptor<EmiSchedule> captor =
                ArgumentCaptor.forClass(EmiSchedule.class);

        verify(emiScheduleRepository, times(12))
                .save(captor.capture());

        assertEquals(
                12,
                captor.getAllValues().size()
        );

        for (int i = 0; i < 12; i++) {

            EmiSchedule emi =
                    captor.getAllValues().get(i);

            assertEquals(
                    i + 1,
                    emi.getInstallmentNo()
            );

            assertEquals(
                    disbursementDate.plusMonths(i + 1),
                    emi.getDueDate()
            );

            assertEquals(
                    EmiStatus.PENDING,
                    emi.getStatus()
            );
        }
    }

    @Test
    void disburse_shouldGenerateZeroInterestEmiWhenInterestRateIsZero() {

        approval.setInterestRate(
                BigDecimal.ZERO
        );

        when(approvalService.getByApplication(application))
                .thenReturn(approval);

        when(disbursementRepository.save(any(Disbursement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(emiScheduleRepository.save(any(EmiSchedule.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(applicationRepository.save(application))
                .thenReturn(application);

        disbursementService.disburse(
                application,
                manager,
                disbursementDate
        );

        ArgumentCaptor<EmiSchedule> captor =
                ArgumentCaptor.forClass(EmiSchedule.class);

        verify(emiScheduleRepository, times(12))
                .save(captor.capture());

        EmiSchedule firstEmi =
                captor.getAllValues().get(0);

        assertEquals(
                new BigDecimal("8333.33"),
                firstEmi.getEmiAmount()
        );

        assertEquals(
                BigDecimal.ZERO,
                firstEmi.getInterestComponent()
        );
    }
}