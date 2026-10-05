package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.ApplicantProfileRepository;
import com.nexturn.lms.repository.LoanApplicationRepository;
import com.nexturn.lms.repository.LoanProductRepository;
import com.nexturn.lms.utils.ApplicationStatus;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceImplTest {

    @Mock
    private LoanApplicationRepository applicationRepository;

    @Mock
    private LoanProductRepository productRepository;

    @Mock
    private ApplicantProfileRepository profileRepository;

    @Mock
    private EligibilityRuleService eligibilityRuleService;

    @InjectMocks
    private LoanApplicationServiceImpl loanApplicationService;

    private User applicant;
    private LoanProduct product;
    private ApplicantProfile profile;
    private LoanApplication application;

    @BeforeEach
    void setUp() {

        applicant = new User();
        applicant.setUserId(1);

        product = new LoanProduct();
        product.setProductId(1);
        product.setMinAmount(new BigDecimal("100000"));
        product.setMaxAmount(new BigDecimal("1000000"));
        product.setMinTenureMonths(12);
        product.setMaxTenureMonths(84);

        profile = new ApplicantProfile();
        profile.setProfileId(1);
        profile.setUser(applicant);

        application = new LoanApplication();
        application.setApplicationId(1);
        application.setApplicant(applicant);
        application.setProduct(product);
        application.setRequestedAmount(new BigDecimal("500000"));
        application.setTenureMonths(60);
        application.setPurpose("Home renovation");
    }

    @Test
    void submitApplication_shouldCreateApplicationAndCalculateEligibility() {

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        when(applicationRepository.save(any(LoanApplication.class)))
                .thenReturn(application);

        when(profileRepository.findByUser(applicant))
                .thenReturn(Optional.of(profile));

        when(eligibilityRuleService.calculateEligibilityScore(
                profile,
                application))
                .thenReturn(80);

        LoanApplication result = loanApplicationService.submitApplication(
                applicant,
                1,
                new BigDecimal("500000"),
                60,
                "Home renovation"
        );

        assertNotNull(result);

        assertEquals(applicant, result.getApplicant());
        assertEquals(product, result.getProduct());
        assertEquals(new BigDecimal("500000"), result.getRequestedAmount());
        assertEquals(60, result.getTenureMonths());
        assertEquals("Home renovation", result.getPurpose());
        assertEquals(ApplicationStatus.SUBMITTED, result.getStatus());
        assertEquals(80, result.getEligibilityScore());

        verify(productRepository).findById(1);

        verify(applicationRepository, times(2))
                .save(any(LoanApplication.class));

        verify(profileRepository)
                .findByUser(applicant);

        verify(eligibilityRuleService)
                .calculateEligibilityScore(profile, application);
    }

    @Test
    void getById_shouldReturnApplication() {

        when(applicationRepository.findById(1))
                .thenReturn(Optional.of(application));

        LoanApplication result =
                loanApplicationService.getById(1);

        assertNotNull(result);
        assertEquals(application, result);

        verify(applicationRepository)
                .findById(1);
    }

    @Test
    void getByApplicant_shouldReturnApplications() {

        when(applicationRepository.findByApplicant_UserId(1))
                .thenReturn(java.util.List.of(application));

        var result =
                loanApplicationService.getByApplicant(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(application, result.get(0));

        verify(applicationRepository)
                .findByApplicant_UserId(1);
    }

    @Test
    void getByStatus_shouldReturnApplications() {

        ApplicationStatus status = ApplicationStatus.SUBMITTED;

        application.setStatus(status);

        when(applicationRepository.findByStatus(status))
                .thenReturn(java.util.List.of(application));

        var result =
                loanApplicationService.getByStatus(status);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(application, result.get(0));

        verify(applicationRepository)
                .findByStatus(status);
    }

    @Test
    void updateStatus_shouldUpdateApplicationStatus() {

        when(applicationRepository.findById(1))
                .thenReturn(Optional.of(application));

        when(applicationRepository.save(application))
                .thenReturn(application);

        LoanApplication result =
                loanApplicationService.updateStatus(
                        1,
                        ApplicationStatus.VERIFIED
                );

        assertNotNull(result);
        assertEquals(ApplicationStatus.VERIFIED, result.getStatus());

        verify(applicationRepository)
                .findById(1);

        verify(applicationRepository)
                .save(application);
    }
}