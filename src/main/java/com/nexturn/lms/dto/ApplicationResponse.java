package com.nexturn.lms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.utils.ApplicationStatus;

public class ApplicationResponse {
    private Integer applicationId;
    private Integer applicantId;
    private String applicantName;
    private Integer productId;
    private BigDecimal requestedAmount;
    private int tenureMonths;
    private String purpose;
    private Integer eligibilityScore;
    private ApplicationStatus status;
    private LocalDateTime submittedAt;

    public ApplicationResponse(LoanApplication app) {
        this.applicationId = app.getApplicationId();
        this.applicantId = app.getApplicant().getUserId();
        this.applicantName = app.getApplicant().getFirstName() + " " + app.getApplicant().getLastName();
        this.productId = app.getProduct().getProductId();
        this.requestedAmount = app.getRequestedAmount();
        this.tenureMonths = app.getTenureMonths();
        this.purpose = app.getPurpose();
        this.eligibilityScore = app.getEligibilityScore();
        this.status = app.getStatus();
        this.submittedAt = app.getSubmittedAt();
    }

    public Integer getApplicationId() { return applicationId; }
    public Integer getApplicantId() { return applicantId; }
    public String getApplicantName() { return applicantName; }
    public Integer getProductId() { return productId; }
    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public int getTenureMonths() { return tenureMonths; }
    public String getPurpose() { return purpose; }
    public Integer getEligibilityScore() { return eligibilityScore; }
    public ApplicationStatus getStatus() { return status; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
}