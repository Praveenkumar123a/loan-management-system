package com.nexturn.lms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nexturn.lms.entity.Approval;

public class ApprovalResponse {
    private Integer approvalId;
    private Integer applicationId;
    private String managerName;
    private String decision;
    private String comments;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;
    private LocalDateTime decidedAt;

    public ApprovalResponse(Approval approval) {
        this.approvalId = approval.getApprovalId();
        this.applicationId = approval.getApplication().getApplicationId();
        this.managerName = approval.getManager().getFirstName() + " " + approval.getManager().getLastName();
        this.decision = approval.getDecision();
        this.comments = approval.getComments();
        this.approvedAmount = approval.getApprovedAmount();
        this.interestRate = approval.getInterestRate();
        this.tenureMonths = approval.getTenureMonths();
        this.decidedAt = approval.getDecidedAt();
    }

    public Integer getApprovalId() { return approvalId; }
    public Integer getApplicationId() { return applicationId; }
    public String getManagerName() { return managerName; }
    public String getDecision() { return decision; }
    public String getComments() { return comments; }
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public BigDecimal getInterestRate() { return interestRate; }
    public Integer getTenureMonths() { return tenureMonths; }
    public LocalDateTime getDecidedAt() { return decidedAt; }
}