package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.Recommendation;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.LoanApplicationRepository;
import com.nexturn.lms.repository.RecommendationRepository;
import com.nexturn.lms.utils.ApplicationStatus;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceImplTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private LoanApplicationRepository applicationRepository;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    private LoanApplication application;
    private User officer;

    @BeforeEach
    void setUp() {

        application = new LoanApplication();
        application.setApplicationId(1);
        application.setStatus(ApplicationStatus.VERIFIED);

        officer = new User();
        officer.setUserId(2);
    }

    @Test
    void recommend_shouldRecommendApplication() {

        when(recommendationRepository.save(any(Recommendation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(applicationRepository.save(application))
                .thenReturn(application);

        Recommendation result =
                recommendationService.recommend(
                        application,
                        officer,
                        true,
                        "Applicant is eligible"
                );

        assertNotNull(result);

        assertEquals(
                application,
                result.getApplication()
        );

        assertEquals(
                officer,
                result.getOfficer()
        );

        assertEquals(
                "RECOMMEND",
                result.getDecision()
        );

        assertEquals(
                "Applicant is eligible",
                result.getComments()
        );

        assertEquals(
                ApplicationStatus.RECOMMENDED,
                application.getStatus()
        );

        verify(recommendationRepository)
                .save(any(Recommendation.class));

        verify(applicationRepository)
                .save(application);
    }

    @Test
    void recommend_shouldNotRecommendApplication() {

        when(recommendationRepository.save(any(Recommendation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(applicationRepository.save(application))
                .thenReturn(application);

        Recommendation result =
                recommendationService.recommend(
                        application,
                        officer,
                        false,
                        "Eligibility criteria not satisfied"
                );

        assertNotNull(result);

        assertEquals(
                "NOT_RECOMMEND",
                result.getDecision()
        );

        assertEquals(
                "Eligibility criteria not satisfied",
                result.getComments()
        );

        assertEquals(
                ApplicationStatus.NOT_RECOMMENDED,
                application.getStatus()
        );

        verify(recommendationRepository)
                .save(any(Recommendation.class));

        verify(applicationRepository)
                .save(application);
    }

    @Test
    void getByApplication_shouldReturnRecommendation() {

        Recommendation recommendation =
                new Recommendation();

        recommendation.setApplication(application);
        recommendation.setOfficer(officer);
        recommendation.setDecision("RECOMMEND");
        recommendation.setComments("Eligible");

        when(recommendationRepository
                .findByApplication(application))
                .thenReturn(Optional.of(recommendation));

        Recommendation result =
                recommendationService.getByApplication(
                        application
                );

        assertNotNull(result);

        assertEquals(
                recommendation,
                result
        );

        assertEquals(
                "RECOMMEND",
                result.getDecision()
        );

        verify(recommendationRepository)
                .findByApplication(application);
    }

    @Test
    void getByApplication_shouldThrowExceptionWhenNotFound() {

        when(recommendationRepository
                .findByApplication(application))
                .thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
                com.nexturn.lms.exception.ResourceNotFoundException.class,
                () -> recommendationService
                        .getByApplication(application)
        );

        verify(recommendationRepository)
                .findByApplication(application);
    }
}