package com.nexturn.lms.dto;

import java.math.BigDecimal;

public class ApprovalRequest {
    private Integer managerId;
    private boolean approve;
    private String comments;
    private BigDecimal approvedAmount;
    private BigDecimal interestRate;
    private Integer tenureMonths;

    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }
    public boolean isApprove() { return approve; }
    public void setApprove(boolean approve) { this.approve = approve; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }
}