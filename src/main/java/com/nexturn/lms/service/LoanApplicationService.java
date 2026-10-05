package com.nexturn.lms.service;

import java.math.BigDecimal;
import java.util.List;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.utils.ApplicationStatus;

public interface LoanApplicationService {
    LoanApplication submitApplication(User applicant,Integer productId,BigDecimal requestedAmount,int tenureMonths,String purpose);
    LoanApplication getById(Integer applicationId);
    List<LoanApplication> getByApplicant(Integer applicantId);
    List<LoanApplication> getByStatus(ApplicationStatus status);
    LoanApplication updateStatus(Integer applicationId,ApplicationStatus newStatus);
}