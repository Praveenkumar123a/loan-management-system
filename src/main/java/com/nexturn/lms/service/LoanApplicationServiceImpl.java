package com.nexturn.lms.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.ApplicantProfileRepository;
import com.nexturn.lms.repository.LoanApplicationRepository;
import com.nexturn.lms.repository.LoanProductRepository;
import com.nexturn.lms.utils.ApplicationStatus;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    private LoanApplicationRepository applicationRepository;

    @Autowired
    private LoanProductRepository productRepository;

    @Autowired
    private ApplicantProfileRepository profileRepository;

    @Autowired
    private EligibilityRuleService eligibilityRuleService;

    @Override
    public LoanApplication submitApplication(User applicant,Integer productId,BigDecimal requestedAmount,int tenureMonths,String purpose) {
        LoanProduct product=productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Loan product not found: "+productId));

        if(requestedAmount.compareTo(product.getMinAmount())<0 || requestedAmount.compareTo(product.getMaxAmount())>0)
            throw new IllegalArgumentException("Requested amount outside product's allowed range");

        if(tenureMonths<product.getMinTenureMonths() || tenureMonths>product.getMaxTenureMonths())
            throw new IllegalArgumentException("Requested tenure outside product's allowed range");

        LoanApplication application=new LoanApplication();
        application.setApplicant(applicant);
        application.setProduct(product);
        application.setRequestedAmount(requestedAmount);
        application.setTenureMonths(tenureMonths);
        application.setPurpose(purpose);
        application.setStatus(ApplicationStatus.SUBMITTED);
        application.setSubmittedAt(LocalDateTime.now());

        LoanApplication saved=applicationRepository.save(application);

        ApplicantProfile profile=profileRepository.findByUser(applicant)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant profile not found — complete KYC first"));

        int score=eligibilityRuleService.calculateEligibilityScore(profile,saved);
        saved.setEligibilityScore(score);

        return applicationRepository.save(saved);
    }

    @Override
    public LoanApplication getById(Integer applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: "+applicationId));
    }

    @Override
    public List<LoanApplication> getByApplicant(Integer applicantId) {
        return applicationRepository.findByApplicant_UserId(applicantId);
    }

    @Override
    public List<LoanApplication> getByStatus(ApplicationStatus status) {
        return applicationRepository.findByStatus(status);
    }

    @Override
    public LoanApplication updateStatus(Integer applicationId,ApplicationStatus newStatus) {
        LoanApplication application=getById(applicationId);
        application.setStatus(newStatus);
        return applicationRepository.save(application);
    }
}