package com.nexturn.lms.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.EligibilityRule;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.repository.EligibilityRuleRepository;


@Service
public class EligibilityRuleServiceImpl implements EligibilityRuleService {

    @Autowired
    private EligibilityRuleRepository ruleRepository;

    @Override
    public int calculateEligibilityScore(ApplicantProfile profile, LoanApplication application) {
        List<EligibilityRule> activeRules = ruleRepository.findByIsActiveTrue();
        int totalScore = 0;

        
        BigDecimal approxMonthlyEmi = application.getRequestedAmount()
                .divide(BigDecimal.valueOf(Math.max(application.getTenureMonths(), 1)), 2, BigDecimal.ROUND_HALF_UP);

        for (EligibilityRule rule : activeRules) {
            switch (rule.getRuleKey()) {
                case "INCOME_TO_LOAN" -> {
                    if (profile.getMonthlyIncome().compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal ratio = profile.getMonthlyIncome()
                                .divide(approxMonthlyEmi, 2, BigDecimal.ROUND_HALF_UP);
                        if (ratio.compareTo(rule.getThresholdValue()) >= 0) {
                            totalScore += rule.getScorePoints();
                        }
                    }
                }
                case "LIABILITY_RATIO" -> {
                    BigDecimal liabilities = profile.getExistingLiabilities() == null
                            ? BigDecimal.ZERO : profile.getExistingLiabilities();
                    if (profile.getMonthlyIncome().compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal ratio = liabilities.divide(profile.getMonthlyIncome(), 2, BigDecimal.ROUND_HALF_UP);
                        if (ratio.compareTo(rule.getThresholdValue()) <= 0) {
                            totalScore += rule.getScorePoints();
                        }
                    }
                }
                default -> {
                    
                }
            }
        }
        return totalScore;
    }
}