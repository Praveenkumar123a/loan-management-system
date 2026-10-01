package com.loan.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recommendations")
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recommendation_id")
    private Integer recommendationId;
    @OneToOne
    @JoinColumn(name = "application_id",nullable = false,unique = true)
    private LoanApplication application;
    @ManyToOne
    @JoinColumn(name = "officer_id", nullable = false)
    private User officer;
    @Column(name = "decision", length = 15, nullable = false)
    private String decision;
    @Column(name = "comments", length = 255, nullable = false)
    private String comments;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    public Recommendation() {
    }
    public Integer getRecommendationId() {
        return recommendationId;
    }
    public void setRecommendationId(Integer recommendationId) {
        this.recommendationId = recommendationId;
    }
    public LoanApplication getApplication() {
        return application;
    }
    public void setApplication(LoanApplication application) {
        this.application = application;
    }
    public User getOfficer() {
        return officer;
    }
    public void setOfficer(User officer) {
        this.officer = officer;
    }
    public String getDecision() {
        return decision;
    }
    public void setDecision(String decision) {
        this.decision = decision;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}