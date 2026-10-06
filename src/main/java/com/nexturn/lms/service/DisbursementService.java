package com.nexturn.lms.service;

import java.time.LocalDate;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;

public interface DisbursementService {
    Disbursement disburse(LoanApplication application, User disbursedBy, LocalDate disbursedDate);

	Disbursement getById(Integer disbursementId);

	Disbursement getByApplication(LoanApplication application);
}