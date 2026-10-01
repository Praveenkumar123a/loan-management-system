package com.nexturn.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.EligibilityRule;

@Repository 
public interface EligibilityRuleRepository extends JpaRepository<EligibilityRule,Integer> {

}
