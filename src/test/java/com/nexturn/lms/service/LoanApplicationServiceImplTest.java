package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
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
import com.nexturn.lms.exception.ResourceNotFoundException;
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
    private LoanApplicationServiceImpl applicationService;

    private User applicant;
    private LoanProduct product;
    private ApplicantProfile profile;
    private LoanApplication application;

    @BeforeEach
    void setUp() {

        applicant = new User();

        applicant.setUserId(1);
        applicant.setFirstName("Punit");
        applicant.setLastName("Kumar");
        applicant.setEmail("punit@example.com");

        product = new LoanProduct();

        product.setProductId(1);
        product.setMinAmount(
                new BigDecimal("10000")
        );
        product.setMaxAmount(
                new BigDecimal("500000")
        );
        product.setMinTenureMonths(6);
        product.setMaxTenureMonths(60);
        product.setDefaultInterestRate(
                new BigDecimal("10.5")
        );
        product.setIsActive(true);

        profile = new ApplicantProfile();
        profile.setProfileId(1);
        profile.setUser(applicant);
        profile.setMonthlyIncome(
                new BigDecimal("50000")
        );
        profile.setExistingLiabilities(
                new BigDecimal("10000")
        );

        application = new LoanApplication();

        application.setApplicationId(1);
        application.setApplicant(applicant);
        application.setProduct(product);
        application.setRequestedAmount(
                new BigDecimal("100000")
        );
        application.setTenureMonths(12);
        application.setPurpose("Personal expenses");
        application.setStatus(
                ApplicationStatus.SUBMITTED
        );
        application.setEligibilityScore(80);
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
                application
        )).thenReturn(80);

        LoanApplication result =
                applicationService.submitApplication(
                        applicant,
                        1,
                        new BigDecimal("100000"),
                        12,
                        "Personal expenses"
                );

        assertNotNull(result);

        assertEquals(
                applicant,
                result.getApplicant()
        );

        assertEquals(
                product,
                result.getProduct()
        );

        assertEquals(
                new BigDecimal("100000"),
                result.getRequestedAmount()
        );

        assertEquals(
                12,
                result.getTenureMonths()
        );

        assertEquals(
                "Personal expenses",
                result.getPurpose()
        );

        assertEquals(
                ApplicationStatus.SUBMITTED,
                result.getStatus()
        );

        assertEquals(
                80,
                result.getEligibilityScore()
        );

        assertNotNull(
                result.getSubmittedAt()
        );

        verify(productRepository)
                .findById(1);

        verify(profileRepository)
                .findByUser(applicant);

        verify(eligibilityRuleService)
                .calculateEligibilityScore(
                        profile,
                        application
                );

        verify(applicationRepository)
                .save(any(LoanApplication.class));
    }

    @Test
    void submitApplication_shouldThrowExceptionWhenProductDoesNotExist() {

        when(productRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> applicationService.submitApplication(
                        applicant,
                        999,
                        new BigDecimal("100000"),
                        12,
                        "Personal expenses"
                )
        );

        verify(productRepository)
                .findById(999);
    }

    @Test
    void submitApplication_shouldThrowExceptionWhenAmountIsBelowMinimum() {

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.submitApplication(
                        applicant,
                        1,
                        new BigDecimal("5000"),
                        12,
                        "Personal expenses"
                )
        );
    }

    @Test
    void submitApplication_shouldThrowExceptionWhenAmountIsAboveMaximum() {

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.submitApplication(
                        applicant,
                        1,
                        new BigDecimal("600000"),
                        12,
                        "Personal expenses"
                )
        );
    }

    @Test
    void submitApplication_shouldThrowExceptionWhenTenureIsBelowMinimum() {

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.submitApplication(
                        applicant,
                        1,
                        new BigDecimal("100000"),
                        3,
                        "Personal expenses"
                )
        );
    }

    @Test
    void submitApplication_shouldThrowExceptionWhenTenureIsAboveMaximum() {

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        assertThrows(
                IllegalArgumentException.class,
                () -> applicationService.submitApplication(
                        applicant,
                        1,
                        new BigDecimal("100000"),
                        72,
                        "Personal expenses"
                )
        );
    }

    @Test
    void submitApplication_shouldThrowExceptionWhenApplicantProfileDoesNotExist() {

        when(productRepository.findById(1))
                .thenReturn(Optional.of(product));

        when(applicationRepository.save(any(LoanApplication.class)))
                .thenReturn(application);

        when(profileRepository.findByUser(applicant))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> applicationService.submitApplication(
                        applicant,
                        1,
                        new BigDecimal("100000"),
                        12,
                        "Personal expenses"
                )
        );

        verify(profileRepository)
                .findByUser(applicant);
    }

    @Test
    void getById_shouldReturnApplication() {

        when(applicationRepository.findById(1))
                .thenReturn(Optional.of(application));

        LoanApplication result =
                applicationService.getById(1);

        assertNotNull(result);

        assertEquals(
                1,
                result.getApplicationId()
        );

        assertEquals(
                new BigDecimal("100000"),
                result.getRequestedAmount()
        );

        verify(applicationRepository)
                .findById(1);
    }

    @Test
    void getById_shouldThrowExceptionWhenApplicationDoesNotExist() {

        when(applicationRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> applicationService.getById(999)
        );

        verify(applicationRepository)
                .findById(999);
    }

    @Test
    void getByApplicant_shouldReturnApplications() {

        when(applicationRepository
                .findByApplicant_UserId(1))
                .thenReturn(List.of(application));

        List<LoanApplication> result =
                applicationService.getByApplicant(1);

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                1,
                result.get(0).getApplicationId()
        );

        verify(applicationRepository)
                .findByApplicant_UserId(1);
    }

    @Test
    void getByStatus_shouldReturnApplications() {

        when(applicationRepository
                .findByStatus(ApplicationStatus.SUBMITTED))
                .thenReturn(List.of(application));

        List<LoanApplication> result =
                applicationService.getByStatus(
                        ApplicationStatus.SUBMITTED
                );

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                ApplicationStatus.SUBMITTED,
                result.get(0).getStatus()
        );

        verify(applicationRepository)
                .findByStatus(ApplicationStatus.SUBMITTED);
    }

    @Test
    void updateStatus_shouldUpdateApplicationStatus() {

        when(applicationRepository.findById(1))
                .thenReturn(Optional.of(application));

        when(applicationRepository.save(application))
                .thenReturn(application);

        LoanApplication result =
                applicationService.updateStatus(
                        1,
                        ApplicationStatus.VERIFIED
                );

        assertNotNull(result);

        assertEquals(
                ApplicationStatus.VERIFIED,
                result.getStatus()
        );

        verify(applicationRepository)
                .findById(1);

        verify(applicationRepository)
                .save(application);
    }
}