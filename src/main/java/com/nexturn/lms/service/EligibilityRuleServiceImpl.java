package com.nexturn.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.EligibilityRule;
import com.nexturn.lms.repository.EligibilityRuleRepository;

@Service
public class EligibilityRuleServiceImpl implements EligibilityRuleService {
   
	private final EligibilityRuleRepository eligibilityRuleRepository;
	public EligibilityRuleServiceImpl(EligibilityRuleRepository eligibilityRuleRepository) {
		this.eligibilityRuleRepository = eligibilityRuleRepository;
	}

	@Override
	public EligibilityRule saveEligibilityRule(EligibilityRule eligibilityRule) {
		return eligibilityRuleRepository.save(eligibilityRule);
	}

	@Override
	public List<EligibilityRule> getAllEligibilityRules() {
		return eligibilityRuleRepository.findAll();
	}

	@Override
	public EligibilityRule getEligibilityRuleById(Integer ruleId) {
		return eligibilityRuleRepository.findById(ruleId)
				.orElseThrow(() ->new RuntimeException("Eligibility Rule is not found"));
	}
	@Override
	public EligibilityRule updateEligibilityRule(Integer ruleId, EligibilityRule eligibilityRule) {
		 EligibilityRule existingRule = eligibilityRuleRepository.findById(ruleId)
			.orElseThrow(() ->new RuntimeException("Eligibility Rule is not found"));
		 existingRule.setRuleId(eligibilityRule.getRuleId());
		 existingRule.setRuleName(eligibilityRule.getRuleName());
		 existingRule.setRuleKey(eligibilityRule.getRuleKey());
		 existingRule.setThresholdValue(eligibilityRule.getThresholdValue());
		 existingRule.setScorePoints(eligibilityRule.getScorePoints());
		 existingRule.setIsActive(eligibilityRule.getIsActive());
		return eligibilityRuleRepository.save(existingRule);
	}

	@Override
	public void deleteEligibilityRule(Integer ruleId) {
       eligibilityRuleRepository.deleteById(ruleId);
	}
	
	
	

}
