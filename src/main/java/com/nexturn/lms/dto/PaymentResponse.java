package com.nexturn.lms.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.nexturn.lms.entity.Payment;

public class PaymentResponse {
    private Integer paymentId;
    private Integer emiId;
    private BigDecimal amountPaid;
    private BigDecimal penaltyPaid;
    private LocalDateTime paymentDate;
    private String paymentMode;
    private String transactionRef;

    public PaymentResponse(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.emiId = payment.getEmiSchedule().getEmiId();
        this.amountPaid = payment.getAmountPaid();
        this.penaltyPaid = payment.getPenaltyPaid();
        this.paymentDate = payment.getPaymentDate();
        this.paymentMode = payment.getPaymentMode();
        this.transactionRef = payment.getTransactionRef();
    }

    public Integer getPaymentId() { return paymentId; }
    public Integer getEmiId() { return emiId; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public BigDecimal getPenaltyPaid() { return penaltyPaid; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public String getPaymentMode() { return paymentMode; }
    public String getTransactionRef() { return transactionRef; }
}