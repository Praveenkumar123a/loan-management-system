package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.Recommendation;

public interface RecommendationService {

    Recommendation createRecommendation(
            Recommendation recommendation);

    List<Recommendation> getAllRecommendations();

    Recommendation getRecommendationById(
            Integer recommendationId);

    List<Recommendation> getRecommendationsByApplication(
            Integer applicationId);

    Recommendation updateRecommendation(
            Integer recommendationId,
            Recommendation recommendation);

    void deleteRecommendation(Integer recommendationId);
}