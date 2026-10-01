package com.loan.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
@Table(name = "disbursements")
public class Disbursement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "disbursement_id")
    private Integer disbursementId;
    @OneToOne
    @JoinColumn(name = "application_id",nullable = false,unique = true)
    private LoanApplication application;
    @Column(name = "disbursed_amount",nullable = false,precision = 12,scale = 2)
    private BigDecimal disbursedAmount;
    @Column(name = "disbursed_date", nullable = false)
    private LocalDate disbursedDate;
    @ManyToOne
    @JoinColumn(name = "disbursed_by",nullable = false)
    private User disbursedBy;
    public Disbursement() {
    }
    public Integer getDisbursementId() {
        return disbursementId;
    }
    public void setDisbursementId(Integer disbursementId) {
        this.disbursementId = disbursementId;
    }
    public LoanApplication getApplication() {
        return application;
    }
    public void setApplication(LoanApplication application) {
        this.application = application;
    }
    public BigDecimal getDisbursedAmount() {
        return disbursedAmount;
    }
    public void setDisbursedAmount(BigDecimal disbursedAmount) {
        this.disbursedAmount = disbursedAmount;
    }
    public LocalDate getDisbursedDate() {
        return disbursedDate;
    }
    public void setDisbursedDate(LocalDate disbursedDate) {
        this.disbursedDate = disbursedDate;
    }
    public User getDisbursedBy() {
        return disbursedBy;
    }
    public void setDisbursedBy(User disbursedBy) {
        this.disbursedBy = disbursedBy;
    }
}