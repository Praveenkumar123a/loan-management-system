package com.nexturn.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.Recommendation;
import com.nexturn.lms.repository.RecommendationRepository;

@Service
public class RecommendationServiceImpl
        implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationServiceImpl(
            RecommendationRepository recommendationRepository) {

        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Recommendation createRecommendation(
            Recommendation recommendation) {

        if (recommendation == null) {
            throw new IllegalArgumentException(
                    "Recommendation cannot be null");
        }

        return recommendationRepository.save(recommendation);
    }

    @Override
    public List<Recommendation> getAllRecommendations() {

        return recommendationRepository.findAll();
    }

    @Override
    public Recommendation getRecommendationById(
            Integer recommendationId) {

        return recommendationRepository.findById(recommendationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Recommendation not found"));
    }

    @Override
    public List<Recommendation> getRecommendationsByApplication(
            Integer applicationId) {

        return recommendationRepository.findAll()
                .stream()
                .filter(recommendation ->
                        recommendation.getApplication() != null
                        && recommendation.getApplication()
                                .getApplicationId() == applicationId)
                .toList();
    }

    @Override
    public Recommendation updateRecommendation(
            Integer recommendationId,
            Recommendation recommendation) {

        Recommendation existingRecommendation =
                recommendationRepository.findById(recommendationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recommendation not found"));

        existingRecommendation.setApplication(
                recommendation.getApplication());

        existingRecommendation.setOfficer(
                recommendation.getOfficer());

        existingRecommendation.setDecision(
                recommendation.getDecision());

        existingRecommendation.setComments(
                recommendation.getComments());

        return recommendationRepository.save(
                existingRecommendation);
    }

    @Override
    public void deleteRecommendation(
            Integer recommendationId) {

        Recommendation existingRecommendation =
                recommendationRepository.findById(recommendationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Recommendation not found"));

        recommendationRepository.delete(
                existingRecommendation);
    }
}