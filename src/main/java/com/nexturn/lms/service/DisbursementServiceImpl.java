package com.nexturn.lms.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class DisbursementServiceImpl implements DisbursementService {

    @Autowired
    private DisbursementRepository disbursementRepository;

    @Autowired
    private EmiScheduleRepository emiScheduleRepository;

    @Autowired
    private LoanApplicationRepository applicationRepository;

    @Autowired
    private ApprovalService approvalService;

    @Override
    public Disbursement disburse(LoanApplication application, User disbursedBy, LocalDate disbursedDate) {

        Approval approval = approvalService.getByApplication(application);

        Disbursement disbursement = new Disbursement();
        disbursement.setApplication(application);
        disbursement.setDisbursedAmount(approval.getApprovedAmount());
        disbursement.setDisbursedDate(disbursedDate);
        disbursement.setDisbursedBy(disbursedBy);

        Disbursement savedDisbursement = disbursementRepository.save(disbursement);

        generateEmiSchedule(savedDisbursement, approval.getApprovedAmount(),
                approval.getInterestRate(), approval.getTenureMonths(), disbursedDate);

        application.setStatus(ApplicationStatus.DISBURSED);
        applicationRepository.save(application);

        return savedDisbursement;
    }

    
    private void generateEmiSchedule(Disbursement disbursement, BigDecimal principal,
                                      BigDecimal annualRate, int tenureMonths, LocalDate startDate) {

        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP); 

        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate);
        BigDecimal onePlusRPowN = onePlusR.pow(tenureMonths);

        BigDecimal numerator = principal.multiply(monthlyRate).multiply(onePlusRPowN);
        BigDecimal denominator = onePlusRPowN.subtract(BigDecimal.ONE);

        BigDecimal emiAmount = monthlyRate.compareTo(BigDecimal.ZERO) == 0
                ? principal.divide(BigDecimal.valueOf(tenureMonths), 2, RoundingMode.HALF_UP)
                : numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        BigDecimal outstandingBalance = principal;

        for (int i = 1; i <= tenureMonths; i++) {
            BigDecimal interestComponent = outstandingBalance.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalComponent = emiAmount.subtract(interestComponent);

            
            if (i == tenureMonths) {
                principalComponent = outstandingBalance;
                emiAmount = principalComponent.add(interestComponent);
            }

            outstandingBalance = outstandingBalance.subtract(principalComponent);

            EmiSchedule emi = new EmiSchedule();
            emi.setDisbursement(disbursement);
            emi.setInstallmentNo(i);
            emi.setDueDate(startDate.plusMonths(i));
            emi.setEmiAmount(emiAmount);
            emi.setPrincipalComponent(principalComponent);
            emi.setInterestComponent(interestComponent);
            emi.setOutstandingBalance(outstandingBalance.max(BigDecimal.ZERO));
            emi.setPenaltyAmount(BigDecimal.ZERO);
            emi.setStatus(EmiStatus.PENDING);

            emiScheduleRepository.save(emi);
        }
    }
}