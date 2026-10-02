package com.nexturn.lms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nexturn.lms.utils.ApplicationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private int applicationId;
    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private LoanProduct product;
    @Column(name = "requested_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal requestedAmount;
    @Column(name = "tenure_months", nullable = false)
    private int tenureMonths;
    @Column(name = "purpose", length = 255, nullable = false)
    private String purpose;
    @Column(name = "eligibility_score")
    private Integer eligibilityScore;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;
    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;
    public LoanApplication() {
    }
    public int getApplicationId() {
        return applicationId;
    }
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }
    public User getApplicant() {
        return applicant;
    }
    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }
    public LoanProduct getProduct() {
        return product;
    }
    public void setProduct(LoanProduct product) {
        this.product = product;
    }
    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }
    public void setRequestedAmount(BigDecimal requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
    public int getTenureMonths() {
        return tenureMonths;
    }
    public void setTenureMonths(int tenureMonths) {
        this.tenureMonths = tenureMonths;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public Integer getEligibilityScore() {
        return eligibilityScore;
    }
    public void setEligibilityScore(Integer eligibilityScore) {
        this.eligibilityScore = eligibilityScore;
    }
    public ApplicationStatus getStatus() {
        return status;
    }
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }
    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}