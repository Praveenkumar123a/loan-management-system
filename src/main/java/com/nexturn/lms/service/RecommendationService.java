package com.nexturn.lms.service;

import java.util.List;
import com.nexturn.lms.entity.Recommendation;

public interface RecommendationService {
    String addRecommendation(Recommendation recommendation);
    String updateRecommendation(Recommendation recommendation);
    String removeRecommendation(Integer recommendationId);
    List<Recommendation> findAllRecommendations();
    Recommendation findRecommendationById(Integer recommendationId);
}