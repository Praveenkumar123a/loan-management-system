package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.LoanApplication;

public interface LoanApplicationService {

    String addLoanApplication(LoanApplication application);
    String updateLoanApplication(LoanApplication application);
    String removeLoanApplication(Long applicationId);
    List<LoanApplication> findAllLoanApplications();
    LoanApplication findLoanApplicationById(Long applicationId);
}