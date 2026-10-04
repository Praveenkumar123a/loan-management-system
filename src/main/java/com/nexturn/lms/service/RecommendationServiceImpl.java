package com.nexturn.lms.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nexturn.lms.entity.Recommendation;
import com.nexturn.lms.repository.RecommendationRepository;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    @Autowired
    RecommendationRepository repo;

    @Override
    public String addRecommendation(Recommendation recommendation) {
        Recommendation rec = repo.save(recommendation);
        String str = "Recommendation inserted "+ rec.getRecommendationId();
        return str;
    }

    @Override
    public String updateRecommendation(Recommendation recommendation) {
        repo.save(recommendation);
        String str = "Recommendation updated";
        return str;
    }

    @Override
    public String removeRecommendation(Integer recommendationId) {
        repo.deleteById(recommendationId);
        return "Recommendation deleted";
    }

    @Override
    public List<Recommendation> findAllRecommendations() {
        return repo.findAll();
    }

    @Override
    public Recommendation findRecommendationById(Integer recommendationId) {
        Optional<Recommendation> recommendation =repo.findById(recommendationId);
        if (recommendation.isEmpty()) {
            return null;
        }
        return recommendation.get();
    }
}