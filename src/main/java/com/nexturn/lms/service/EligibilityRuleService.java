package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.EligibilityRule;

public interface EligibilityRuleService {

    EligibilityRule createRule(EligibilityRule rule);

    List<EligibilityRule> getAllRules();

    EligibilityRule getRuleById(Integer ruleId);

    EligibilityRule updateRule(
            Integer ruleId,
            EligibilityRule rule);

    void deleteRule(Integer ruleId);
}