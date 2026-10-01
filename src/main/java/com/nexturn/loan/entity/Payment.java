package com.nexturn.loan.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Payment {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer paymentId;
    private EmiSchedule emiSchedule;
    private BigDecimal amountPaid;
    private BigDecimal penaltyPaid;
    private LocalDateTime paymentDate;
    private String paymentMode;
    private String transactionRef;
    
    public Payment(){
    }
    public Payment(Integer paymentId, EmiSchedule emiSchedule, BigDecimal amountPaid, BigDecimal penaltyPaid,
            LocalDateTime paymentDate, String paymentMode, String transactionRef) {
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
