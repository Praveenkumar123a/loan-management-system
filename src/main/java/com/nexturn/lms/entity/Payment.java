package com.nexturn.lms.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Integer paymentId;
    @ManyToOne
    @JoinColumn(name = "emi_id", nullable = false)
    private EmiSchedule emiSchedule;
    @Column(name = "amount_paid", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountPaid;
    @Column(name = "penalty_paid", precision = 10, scale = 2)
    private BigDecimal penaltyPaid;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    @Column(name = "payment_mode", length = 20, nullable = false)
    private String paymentMode;
    @Column(name = "transaction_ref", length = 40, nullable = false, unique = true)
    private String transactionRef;
    public Payment() {
    }
    public Payment(Integer paymentId, EmiSchedule emiSchedule,
                   BigDecimal amountPaid, BigDecimal penaltyPaid,
                   LocalDateTime paymentDate, String paymentMode,
                   String transactionRef) {
        this.paymentId = paymentId;
        this.emiSchedule = emiSchedule;
        this.amountPaid = amountPaid;
        this.penaltyPaid = penaltyPaid;
        this.paymentDate = paymentDate;
        this.paymentMode = paymentMode;
        this.transactionRef = transactionRef;
    }
    public Integer getPaymentId() {
        return paymentId;
    }
    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }
    public EmiSchedule getEmiSchedule() {
        return emiSchedule;
    }
    public void setEmiSchedule(EmiSchedule emiSchedule) {
        this.emiSchedule = emiSchedule;
    }
    public BigDecimal getAmountPaid() {
        return amountPaid;
    }
    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }
    public BigDecimal getPenaltyPaid() {
        return penaltyPaid;
    }
    public void setPenaltyPaid(BigDecimal penaltyPaid) {
        this.penaltyPaid = penaltyPaid;
    }
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
    public String getPaymentMode() {
        return paymentMode;
    }
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
    public String getTransactionRef() {
        return transactionRef;
    }
    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }
}