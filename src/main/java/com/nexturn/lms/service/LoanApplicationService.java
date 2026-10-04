package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.LoanApplication;

public interface LoanApplicationService {

    LoanApplication createApplication(LoanApplication application);

    List<LoanApplication> getAllApplications();

    LoanApplication getApplicationById(Integer applicationId);

    LoanApplication updateApplication(
            Integer applicationId,
            LoanApplication application);

    void deleteApplication(Integer applicationId);
}