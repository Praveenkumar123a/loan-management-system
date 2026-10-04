package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexturn.lms.entity.EligibilityRule;
import com.nexturn.lms.service.EligibilityRuleService;

@RestController
@RequestMapping("/api/eligibility-rules")
public class EligibilityRuleController {

    private final EligibilityRuleService eligibilityRuleService;

    public EligibilityRuleController(
            EligibilityRuleService eligibilityRuleService) {

        this.eligibilityRuleService = eligibilityRuleService;
    }

    @PostMapping
    public ResponseEntity<EligibilityRule> createRule(
            @RequestBody EligibilityRule rule) {

        EligibilityRule savedRule =
                eligibilityRuleService.createRule(rule);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedRule);
    }

    @GetMapping
    public ResponseEntity<List<EligibilityRule>> getAllRules() {

        return ResponseEntity.ok(
                eligibilityRuleService.getAllRules());
    }

    @GetMapping("/{ruleId}")
    public ResponseEntity<EligibilityRule> getRuleById(
            @PathVariable Integer ruleId) {

        return ResponseEntity.ok(
                eligibilityRuleService.getRuleById(ruleId));
    }

    @PutMapping("/{ruleId}")
    public ResponseEntity<EligibilityRule> updateRule(
            @PathVariable Integer ruleId,
            @RequestBody EligibilityRule rule) {

        return ResponseEntity.ok(
                eligibilityRuleService.updateRule(
                        ruleId,
                        rule));
    }

    @DeleteMapping("/{ruleId}")
    public ResponseEntity<Void> deleteRule(
            @PathVariable Integer ruleId) {

        eligibilityRuleService.deleteRule(ruleId);

        return ResponseEntity.noContent().build();
    }
}