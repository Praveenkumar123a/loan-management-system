package com.nexturn.lms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "approvals")
public class Approval {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "approval_id")
	private Integer approvalId;
	@OneToOne
	@JoinColumn(name = "application_id",nullable = false,unique = true)
	private LoanApplication application;
	@ManyToOne
	@JoinColumn(name = "manager_id",nullable = false)
	private User manager;
	@Column(length = 10, nullable = false)
	private String decision;
	@Column(length= 255)
	private String comments;
	@Column(name = "approved_amount", precision = 12, scale = 2)
	private BigDecimal approvedAmount;
	@Column(name = "interest_rate", precision = 5, scale = 2)
	private BigDecimal interestRate;
	@Column(name = "tenure_months")
	private Integer tenureMonths;
	@CreationTimestamp
	@Column(name = "decided_at")
	private LocalDateTime decidedAt;
	public Approval() {
	}
	public Integer getApprovalId() {
		return approvalId;
	}
	public void setApprovalId(Integer approvalId) {
		this.approvalId = approvalId;
	}
	public LoanApplication getApplication() {
		return application;
	}
	public void setApplication(LoanApplication application) {
		this.application = application;
	}
	public User getManager() {
		return manager;
	}
	public void setManager(User manager) {
		this.manager = manager;
	}
	public String getDecision() {
		return decision;
	}
	public void setDecision(String decision) {
		this.decision = decision;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public BigDecimal getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(BigDecimal approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	public Integer getTenureMonths() {
		return tenureMonths;
	}
	public void setTenureMonths(Integer tenureMonths) {
		this.tenureMonths = tenureMonths;
	}
	public LocalDateTime getDecidedAt() {
		return decidedAt;
	}
	public void setDecidedAt(LocalDateTime decidedAt) {
		this.decidedAt = decidedAt;
	}
	public Approval(Integer approvalId, LoanApplication application, User manager, String decision, String comments,
			BigDecimal approvedAmount, BigDecimal interestRate, Integer tenureMonths, LocalDateTime decidedAt) {
		super();
		this.approvalId = approvalId;
		this.application = application;
		this.manager = manager;
		this.decision = decision;
		this.comments = comments;
		this.approvedAmount = approvedAmount;
		this.interestRate = interestRate;
		this.tenureMonths = tenureMonths;
		this.decidedAt = decidedAt;
	}
	
}
