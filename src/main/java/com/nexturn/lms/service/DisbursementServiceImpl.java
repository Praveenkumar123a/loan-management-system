package com.nexturn.lms.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.Approval;
import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.DisbursementRepository;
import com.nexturn.lms.repository.LoanApplicationRepository;
import com.nexturn.lms.utils.ApplicationStatus;

import jakarta.transaction.Transactional;


@Service
public class DisbursementServiceImpl implements DisbursementService {

    @Autowired
    private DisbursementRepository disbursementRepository;

    @Autowired
    private LoanApplicationRepository applicationRepository;

    @Autowired
    private ApprovalService approvalService;
    
    @Autowired
    private EmiScheduleService emiScheduleService;
    @Transactional
    @Override
    public Disbursement disburse(LoanApplication application, User disbursedBy, LocalDate disbursedDate) {

        Approval approval = approvalService.getByApplication(application);

        Disbursement disbursement = new Disbursement();
        disbursement.setApplication(application);
        disbursement.setDisbursedAmount(approval.getApprovedAmount());
        disbursement.setDisbursedDate(disbursedDate);
        disbursement.setDisbursedBy(disbursedBy);

        Disbursement savedDisbursement = disbursementRepository.save(disbursement);

        emiScheduleService.generateEmiSchedule(
                savedDisbursement,
                approval.getApprovedAmount(),
                approval.getInterestRate(),
                approval.getTenureMonths(),
                disbursedDate);

        application.setStatus(ApplicationStatus.DISBURSED);
        applicationRepository.save(application);

        return savedDisbursement;
    }
    
    @Override
    public Disbursement getById(Integer disbursementId) {
        return disbursementRepository.findById(disbursementId) .orElseThrow(() ->new ResourceNotFoundException("Disbursement not found: "+ disbursementId));
    }

    @Override
    public Disbursement getByApplication(LoanApplication application) {
        return disbursementRepository.findByApplication(application).orElseThrow(() -> new ResourceNotFoundException("Disbursement not found for application: "+ application.getApplicationId()));
    }
}