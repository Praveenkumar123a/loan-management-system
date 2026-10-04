package com.nexturn.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.exception.LoanApplicationNotFoundException;
import com.nexturn.lms.repository.LoanApplicationRepository;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;

    public LoanApplicationServiceImpl(
            LoanApplicationRepository loanApplicationRepository) {

        this.loanApplicationRepository = loanApplicationRepository;
    }

    @Override
    public LoanApplication createApplication(
            LoanApplication application) {

        if (application == null) {
            throw new IllegalArgumentException(
                    "Loan application cannot be null");
        }

        return loanApplicationRepository.save(application);
    }

    @Override
    public List<LoanApplication> getAllApplications() {

        return loanApplicationRepository.findAll();
    }

    @Override
    public LoanApplication getApplicationById(
            Integer applicationId) {

        return loanApplicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new LoanApplicationNotFoundException(
                                "Loan application not found"));
    }

    @Override
    public LoanApplication updateApplication(
            Integer applicationId,
            LoanApplication application) {

        LoanApplication existingApplication =
                loanApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new LoanApplicationNotFoundException(
                                        "Loan application not found"));

        existingApplication.setRequestedAmount(
                application.getRequestedAmount());

        existingApplication.setTenureMonths(
                application.getTenureMonths());

        existingApplication.setPurpose(
                application.getPurpose());

        existingApplication.setEligibilityScore(
                application.getEligibilityScore());

        existingApplication.setStatus(
                application.getStatus());

        return loanApplicationRepository.save(
                existingApplication);
    }

    @Override
    public void deleteApplication(
            Integer applicationId) {

        LoanApplication existingApplication =
                loanApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new LoanApplicationNotFoundException(
                                        "Loan application not found"));

        loanApplicationRepository.delete(existingApplication);
    }
}