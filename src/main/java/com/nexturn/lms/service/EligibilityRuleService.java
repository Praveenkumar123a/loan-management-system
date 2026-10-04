package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.EligibilityRule;

public interface EligibilityRuleService {
	EligibilityRule saveEligibilityRule(EligibilityRule eligibilityRule);
	List<EligibilityRule> getAllEligibilityRules();
	EligibilityRule getEligibilityRuleById(Integer ruleId);
	EligibilityRule updateEligibilityRule(Integer ruleId,EligibilityRule eligibilityRule);
	void deleteEligibilityRule(Integer ruleId);

}
