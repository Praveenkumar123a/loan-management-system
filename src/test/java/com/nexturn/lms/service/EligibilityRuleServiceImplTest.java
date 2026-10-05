package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.EligibilityRule;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.repository.EligibilityRuleRepository;

@ExtendWith(MockitoExtension.class)
class EligibilityRuleServiceImplTest {

    @Mock
    private EligibilityRuleRepository ruleRepository;

    @InjectMocks
    private EligibilityRuleServiceImpl ruleService;

    private ApplicantProfile profile;
    private LoanApplication application;

    @BeforeEach
    void setUp() {

        profile = new ApplicantProfile();

        profile.setMonthlyIncome(
                new BigDecimal("50000")
        );

        profile.setExistingLiabilities(
                new BigDecimal("10000")
        );

        application = new LoanApplication();

        application.setRequestedAmount(
                new BigDecimal("100000")
        );

        application.setTenureMonths(12);
    }

    @Test
    void calculateEligibilityScore_shouldAddScoreForIncomeToLoanRule() {

        EligibilityRule rule =
                new EligibilityRule();

        rule.setRuleKey("INCOME_TO_LOAN");

        rule.setThresholdValue(
                new BigDecimal("5.00")
        );

        rule.setScorePoints(50);

        rule.setIsActive(true);

        when(ruleRepository.findByIsActiveTrue())
                .thenReturn(List.of(rule));

        int result =
                ruleService.calculateEligibilityScore(
                        profile,
                        application
                );

        assertEquals(50, result);

        verify(ruleRepository)
                .findByIsActiveTrue();
    }

    @Test
    void calculateEligibilityScore_shouldNotAddScoreWhenIncomeToLoanRatioFails() {

        EligibilityRule rule =
                new EligibilityRule();

        rule.setRuleKey("INCOME_TO_LOAN");

        rule.setThresholdValue(
                new BigDecimal("10.00")
        );

        rule.setScorePoints(50);

        rule.setIsActive(true);

        when(ruleRepository.findByIsActiveTrue())
                .thenReturn(List.of(rule));

        int result =
                ruleService.calculateEligibilityScore(
                        profile,
                        application
                );

        assertEquals(0, result);
    }

    @Test
    void calculateEligibilityScore_shouldAddScoreForLiabilityRatioRule() {

        EligibilityRule rule =
                new EligibilityRule();

        rule.setRuleKey("LIABILITY_RATIO");

        rule.setThresholdValue(
                new BigDecimal("0.30")
        );

        rule.setScorePoints(40);

        rule.setIsActive(true);

        when(ruleRepository.findByIsActiveTrue())
                .thenReturn(List.of(rule));

        int result =
                ruleService.calculateEligibilityScore(
                        profile,
                        application
                );

        assertEquals(40, result);
    }

    @Test
    void calculateEligibilityScore_shouldNotAddScoreWhenLiabilityRatioFails() {

        EligibilityRule rule =
                new EligibilityRule();

        rule.setRuleKey("LIABILITY_RATIO");

        rule.setThresholdValue(
                new BigDecimal("0.10")
        );

        rule.setScorePoints(40);

        rule.setIsActive(true);

        when(ruleRepository.findByIsActiveTrue())
                .thenReturn(List.of(rule));

        int result =
                ruleService.calculateEligibilityScore(
                        profile,
                        application
                );

        assertEquals(0, result);
    }

    @Test
    void calculateEligibilityScore_shouldHandleNullLiabilities() {

        profile.setExistingLiabilities(null);

        EligibilityRule rule =
                new EligibilityRule();

        rule.setRuleKey("LIABILITY_RATIO");

        rule.setThresholdValue(
                new BigDecimal("0.10")
        );

        rule.setScorePoints(40);

        rule.setIsActive(true);

        when(ruleRepository.findByIsActiveTrue())
                .thenReturn(List.of(rule));

        int result =
                ruleService.calculateEligibilityScore(
                        profile,
                        application
                );

        assertEquals(40, result);
    }

    @Test
    void calculateEligibilityScore_shouldReturnZeroWhenNoRulesExist() {

        when(ruleRepository.findByIsActiveTrue())
                .thenReturn(List.of());

        int result =
                ruleService.calculateEligibilityScore(
                        profile,
                        application
                );

        assertEquals(0, result);

        verify(ruleRepository)
                .findByIsActiveTrue();
    }

    @Test
    void calculateEligibilityScore_shouldIgnoreUnknownRuleKey() {

        EligibilityRule rule =
                new EligibilityRule();

        rule.setRuleKey("UNKNOWN_RULE");

        rule.setThresholdValue(
                new BigDecimal("1.00")
        );

        rule.setScorePoints(100);

        rule.setIsActive(true);

        when(ruleRepository.findByIsActiveTrue())
                .thenReturn(List.of(rule));

        int result =
                ruleService.calculateEligibilityScore(
                        profile,
                        application
                );

        assertEquals(0, result);
    }

    @Test
    void calculateEligibilityScore_shouldAddScoresFromMultipleRules() {

        EligibilityRule incomeRule =
                new EligibilityRule();

        incomeRule.setRuleKey("INCOME_TO_LOAN");

        incomeRule.setThresholdValue(
                new BigDecimal("5.00")
        );

        incomeRule.setScorePoints(50);

        EligibilityRule liabilityRule =
                new EligibilityRule();

        liabilityRule.setRuleKey("LIABILITY_RATIO");

        liabilityRule.setThresholdValue(
                new BigDecimal("0.30")
        );

        liabilityRule.setScorePoints(40);

        when(ruleRepository.findByIsActiveTrue())
                .thenReturn(
                        List.of(
                                incomeRule,
                                liabilityRule
                        )
                );

        int result =
                ruleService.calculateEligibilityScore(
                        profile,
                        application
                );

        assertEquals(90, result);
    }
}