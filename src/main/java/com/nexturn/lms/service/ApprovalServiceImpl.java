package com.nexturn.lms.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.Approval;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.ApprovalRepository;
import com.nexturn.lms.repository.LoanApplicationRepository;

import com.nexturn.lms.utils.ApplicationStatus;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private LoanApplicationRepository applicationRepository;

    @Override
    public Approval decide(LoanApplication application, User manager, boolean approve, String comments,
                           BigDecimal approvedAmount, BigDecimal interestRate, Integer tenureMonths) {

        Approval approval = new Approval();
        approval.setApplication(application);
        approval.setManager(manager);
        approval.setDecision(approve ? "APPROVED" : "REJECTED");
        approval.setComments(comments);

        if (approve) {
            approval.setApprovedAmount(approvedAmount);
            approval.setInterestRate(interestRate);
            approval.setTenureMonths(tenureMonths);
        }

        Approval saved = approvalRepository.save(approval);

        application.setStatus(approve ? ApplicationStatus.APPROVED : ApplicationStatus.REJECTED);
        applicationRepository.save(application);

        return saved;
    }

    @Override
    public Approval getByApplication(LoanApplication application) {
        return approvalRepository.findByApplication(application)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No approval decision yet for application: " + application.getApplicationId()));
    }
}