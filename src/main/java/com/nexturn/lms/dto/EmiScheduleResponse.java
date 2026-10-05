package com.nexturn.lms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.utils.EmiStatus;

public class EmiScheduleResponse {
    private Integer emiId;
    private int installmentNo;
    private LocalDate dueDate;
    private BigDecimal emiAmount;
    private BigDecimal principalComponent;
    private BigDecimal interestComponent;
    private BigDecimal outstandingBalance;
    private BigDecimal penaltyAmount;
    private EmiStatus status;
    private LocalDate paidDate;

    public EmiScheduleResponse(EmiSchedule emi) {
        this.emiId = emi.getEmiId();
        this.installmentNo = emi.getInstallmentNo();
        this.dueDate = emi.getDueDate();
        this.emiAmount = emi.getEmiAmount();
        this.principalComponent = emi.getPrincipalComponent();
        this.interestComponent = emi.getInterestComponent();
        this.outstandingBalance = emi.getOutstandingBalance();
        this.penaltyAmount = emi.getPenaltyAmount();
        this.status = emi.getStatus();
        this.paidDate = emi.getPaidDate();
    }

    public Integer getEmiId() { return emiId; }
    public int getInstallmentNo() { return installmentNo; }
    public LocalDate getDueDate() { return dueDate; }
    public BigDecimal getEmiAmount() { return emiAmount; }
    public BigDecimal getPrincipalComponent() { return principalComponent; }
    public BigDecimal getInterestComponent() { return interestComponent; }
    public BigDecimal getOutstandingBalance() { return outstandingBalance; }
    public BigDecimal getPenaltyAmount() { return penaltyAmount; }
    public EmiStatus getStatus() { return status; }
    public LocalDate getPaidDate() { return paidDate; }
}