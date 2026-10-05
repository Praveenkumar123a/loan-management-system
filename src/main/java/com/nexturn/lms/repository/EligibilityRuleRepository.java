package com.nexturn.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.EligibilityRule;

@Repository
public interface EligibilityRuleRepository extends JpaRepository<EligibilityRule, Integer> {

    List<EligibilityRule> findByIsActiveTrue();
}