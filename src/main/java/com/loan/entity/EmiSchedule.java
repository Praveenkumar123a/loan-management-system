package com.loan.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name = "emi_schedule",uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"disbursement_id", "installment_no"}
            )
         }
  )
public class EmiSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "emi_id")
	private Integer emiId;
	@ManyToOne
	@JoinColumn(
			name = "disbursement_id",
			nullable = false
	)
	private Disbursement disbursement;
	@Column(name = "installment_no", nullable = false)
	private Integer installmentNo;
	@Column(name = "due_date", nullable = false)
	private LocalDate dueDate;
	@Column(name = "emi_amount", nullable = false, precision = 12, scale = 2)
	private BigDecimal emiAmount;
	@Column(name = "principal_component", nullable = false, precision = 12, scale = 2)
	private BigDecimal principalComponent;
	@Column(name = "interest_component", nullable = false, precision = 12, scale = 2)
	private BigDecimal interestComponent;
	@Column(name = "outstanding_balance", nullable = false, precision = 12, scale = 2)
	private BigDecimal outstandingBalance;
	 @Column(name = "penalty_amount", precision = 10, scale = 2, nullable = false, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
	 private BigDecimal penaltyAmount = BigDecimal.ZERO;
	 @Enumerated(EnumType.STRING)
	 @Column(name = "status", length = 10, nullable = false)
	 private EmiStatus status = EmiStatus.PENDING;
	 @Column(name = "paid_date")
	 private LocalDate paidDate;
	 
	 public EmiSchedule() {
		super();
	}
	 public Integer getEmiId() {
		 return emiId;
	 }
	 public void setEmiId(Integer emiId) {
		 this.emiId = emiId;
	 }
	 public Disbursement getDisbursement() {
		 return disbursement;
	 }
	 public void setDisbursement(Disbursement disbursement) {
		 this.disbursement = disbursement;
	 }
	 public Integer getInstallmentNo() {
		 return installmentNo;
	 }
	 public void setInstallmentNo(Integer installmentNo) {
		 this.installmentNo = installmentNo;
	 }
	 public LocalDate getDueDate() {
		 return dueDate;
	 }
	 public void setDueDate(LocalDate dueDate) {
		 this.dueDate = dueDate;
	 }
	 public BigDecimal getEmiAmount() {
		 return emiAmount;
	 }
	 public void setEmiAmount(BigDecimal emiAmount) {
		 this.emiAmount = emiAmount;
	 }
	 public BigDecimal getPrincipalComponent() {
		 return principalComponent;
	 }
	 public void setPrincipalComponent(BigDecimal principalComponent) {
		 this.principalComponent = principalComponent;
	 }
	 public BigDecimal getInterestComponent() {
		 return interestComponent;
	 }
	 public void setInterestComponent(BigDecimal interestComponent) {
		 this.interestComponent = interestComponent;
	 }
	 public BigDecimal getOutstandingBalance() {
		 return outstandingBalance;
	 }
	 public void setOutstandingBalance(BigDecimal outstandingBalance) {
		 this.outstandingBalance = outstandingBalance;
	 }
	 public BigDecimal getPenaltyAmount() {
		 return penaltyAmount;
	 }
	 public void setPenaltyAmount(BigDecimal penaltyAmount) {
		 this.penaltyAmount = penaltyAmount;
	 }
	 public EmiStatus getStatus() {
		 return status;
	 }
	 public void setStatus(EmiStatus status) {
		 this.status = status;
	 }
	 public LocalDate getPaidDate() {
		 return paidDate;
	 }
	 public void setPaidDate(LocalDate paidDate) {
		 this.paidDate = paidDate;
	 }
}
