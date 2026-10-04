package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.Approval;

public interface ApprovalService {

    Approval createApproval(Approval approval);

    List<Approval> getAllApprovals();

    Approval getApprovalById(Integer approvalId);

    List<Approval> getApprovalsByApplication(Integer applicationId);

    Approval updateApproval(
            Integer approvalId,
            Approval approval);

    void deleteApproval(Integer approvalId);
}