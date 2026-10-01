package com.nexturn.lms.entity;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "eligibility_rules")
public class EligibilityRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Integer ruleId;
    @Column(name = "rule_name", length = 60, nullable = false)
    private String ruleName;
    @Column(name = "rule_key", length = 40, nullable = false)
    private String ruleKey;
    @Column(name = "threshold_value", precision = 8, scale = 2, nullable = false)
    private BigDecimal thresholdValue;
    @Column(name = "score_points", nullable = false)
    private Integer scorePoints;
    @Column(name = "is_active")
    private Boolean isActive = true;
    public EligibilityRule() {
    }
    public Integer getRuleId() {
        return ruleId;
    }
    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }
    public String getRuleName() {
        return ruleName;
    }
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
    public String getRuleKey() {
        return ruleKey;
    }
    public void setRuleKey(String ruleKey) {
        this.ruleKey = ruleKey;
    }
    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }
    public void setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
    }
    public Integer getScorePoints() {
        return scorePoints;
    }
    public void setScorePoints(Integer scorePoints) {
        this.scorePoints = scorePoints;
    }
    public Boolean getIsActive() {
        return isActive;
    }
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}