package com.nexturn.lms.service;

import java.math.BigDecimal;

import com.nexturn.lms.entity.Approval;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;

public interface ApprovalService {
    Approval decide(LoanApplication application, User manager, boolean approve,
                     String comments, BigDecimal approvedAmount, BigDecimal interestRate, Integer tenureMonths);
    Approval getByApplication(LoanApplication application);
}