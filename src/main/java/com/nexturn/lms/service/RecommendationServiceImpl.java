package com.nexturn.lms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.Recommendation;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.LoanApplicationRepository;
import com.nexturn.lms.repository.RecommendationRepository;

import com.nexturn.lms.utils.ApplicationStatus;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    private RecommendationRepository recommendationRepository;

    @Autowired
    private LoanApplicationRepository applicationRepository;

    @Override
    public Recommendation recommend(LoanApplication application, User officer, boolean recommend, String comments) {
        Recommendation recommendation = new Recommendation();
        recommendation.setApplication(application);
        recommendation.setOfficer(officer);
        recommendation.setDecision(recommend ? "RECOMMEND" : "NOT_RECOMMEND");
        recommendation.setComments(comments);

        Recommendation saved = recommendationRepository.save(recommendation);

        application.setStatus(recommend ? ApplicationStatus.RECOMMENDED : ApplicationStatus.NOT_RECOMMENDED);
        applicationRepository.save(application);

        return saved;
    }

    @Override
    public Recommendation getByApplication(LoanApplication application) {
        return recommendationRepository.findByApplication(application)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No recommendation yet for application: " + application.getApplicationId()));
    }
}