package com.nexturn.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.exception.LoanApplicationNotFoundException;
import com.nexturn.lms.repository.LoanApplicationRepository;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    LoanApplicationRepository repo;

    @Override
    public String addLoanApplication(LoanApplication application) {
        LoanApplication app = repo.save(application);
        String str = "Loan application inserted " + app.getApplicationId();
        return str;
    }

    @Override
    public String updateLoanApplication(LoanApplication application) {
        repo.save(application);
        String str = "Loan application updated";
        return str;
    }

    @Override
    public String removeLoanApplication(Long applicationId) {
        repo.deleteById(applicationId);
        return "Loan application deleted";
    }

    @Override
    public List<LoanApplication> findAllLoanApplications() {
        return repo.findAll();
    }

    @Override
    public LoanApplication findLoanApplicationById(Long applicationId) {
        Optional<LoanApplication> application = repo.findById(applicationId);

        if (application.isEmpty())
            throw new LoanApplicationNotFoundException();

        return application.get();
    }
}