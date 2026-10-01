package com.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.entity.Recommendation;

public interface RecommendationRepository extends JpaRepository<Recommendation,Integer>{

}
