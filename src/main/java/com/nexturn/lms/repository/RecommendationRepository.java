package com.nexturn.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.Recommendation;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {

    Optional<Recommendation> findByApplication(LoanApplication application);
}