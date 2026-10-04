package com.nexturn.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.Approval;
import com.nexturn.lms.repository.ApprovalRepository;

@Service
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;

    public ApprovalServiceImpl(
            ApprovalRepository approvalRepository) {

        this.approvalRepository = approvalRepository;
    }

    @Override
    public Approval createApproval(Approval approval) {

        if (approval == null) {
            throw new IllegalArgumentException(
                    "Approval cannot be null");
        }

        return approvalRepository.save(approval);
    }

    @Override
    public List<Approval> getAllApprovals() {

        return approvalRepository.findAll();
    }

    @Override
    public Approval getApprovalById(Integer approvalId) {

        return approvalRepository.findById(approvalId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Approval not found"));
    }

    @Override
    public List<Approval> getApprovalsByApplication(
            Integer applicationId) {

        return approvalRepository.findAll()
                .stream()
                .filter(approval ->
                        approval.getApplication() != null
                        && approval.getApplication()
                                .getApplicationId() == applicationId)
                .toList();
    }

    @Override
    public Approval updateApproval(
            Integer approvalId,
            Approval approval) {

        Approval existingApproval =
                approvalRepository.findById(approvalId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Approval not found"));

        existingApproval.setApplication(
                approval.getApplication());

        existingApproval.setManager(
                approval.getManager());

        existingApproval.setDecision(
                approval.getDecision());

        existingApproval.setComments(
                approval.getComments());

        existingApproval.setApprovedAmount(
                approval.getApprovedAmount());

        existingApproval.setInterestRate(
                approval.getInterestRate());

        existingApproval.setTenureMonths(
                approval.getTenureMonths());

        return approvalRepository.save(existingApproval);
    }

    @Override
    public void deleteApproval(Integer approvalId) {

        Approval existingApproval =
                approvalRepository.findById(approvalId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Approval not found"));

        approvalRepository.delete(existingApproval);
    }
}