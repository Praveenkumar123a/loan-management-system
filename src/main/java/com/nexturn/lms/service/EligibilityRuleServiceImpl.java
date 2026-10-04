package com.nexturn.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.EligibilityRule;
import com.nexturn.lms.repository.EligibilityRuleRepository;

@Service
public class EligibilityRuleServiceImpl implements EligibilityRuleService {

    private final EligibilityRuleRepository eligibilityRuleRepository;

    public EligibilityRuleServiceImpl(
            EligibilityRuleRepository eligibilityRuleRepository) {

        this.eligibilityRuleRepository = eligibilityRuleRepository;
    }

    @Override
    public EligibilityRule createRule(EligibilityRule rule) {

        if (rule == null) {
            throw new IllegalArgumentException(
                    "Eligibility rule cannot be null");
        }

        return eligibilityRuleRepository.save(rule);
    }

    @Override
    public List<EligibilityRule> getAllRules() {

        return eligibilityRuleRepository.findAll();
    }

    @Override
    public EligibilityRule getRuleById(Integer ruleId) {

        return eligibilityRuleRepository.findById(ruleId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Eligibility rule not found"));
    }

    @Override
    public EligibilityRule updateRule(
            Integer ruleId,
            EligibilityRule rule) {

        EligibilityRule existingRule =
                eligibilityRuleRepository.findById(ruleId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Eligibility rule not found"));

        existingRule.setRuleName(
                rule.getRuleName());

        existingRule.setRuleKey(
                rule.getRuleKey());

        existingRule.setThresholdValue(
                rule.getThresholdValue());

        existingRule.setScorePoints(
                rule.getScorePoints());

        existingRule.setIsActive(
                rule.getIsActive());

        return eligibilityRuleRepository.save(existingRule);
    }

    @Override
    public void deleteRule(Integer ruleId) {

        EligibilityRule existingRule =
                eligibilityRuleRepository.findById(ruleId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Eligibility rule not found"));

        eligibilityRuleRepository.delete(existingRule);
    }
}