package com.nexturn.lms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nexturn.lms.entity.Disbursement;

public class DisbursementResponse {
    private Integer disbursementId;
    private Integer applicationId;
    private BigDecimal disbursedAmount;
    private LocalDate disbursedDate;
    private String disbursedByName;

    public DisbursementResponse(Disbursement d) {
        this.disbursementId = d.getDisbursementId();
        this.applicationId = d.getApplication().getApplicationId();
        this.disbursedAmount = d.getDisbursedAmount();
        this.disbursedDate = d.getDisbursedDate();
        this.disbursedByName = d.getDisbursedBy().getFirstName() + " " + d.getDisbursedBy().getLastName();
    }

    public Integer getDisbursementId() { return disbursementId; }
    public Integer getApplicationId() { return applicationId; }
    public BigDecimal getDisbursedAmount() { return disbursedAmount; }
    public LocalDate getDisbursedDate() { return disbursedDate; }
    public String getDisbursedByName() { return disbursedByName; }
}