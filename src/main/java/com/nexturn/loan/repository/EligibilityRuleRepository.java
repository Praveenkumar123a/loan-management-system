package com.nexturn.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.loan.entity.EligibilityRule;

@Repository 
public interface EligibilityRuleRepository extends JpaRepository<EligibilityRule,Integer> {

}
