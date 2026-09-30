package com.loan.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table
public class AuditLog {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer logId;
    private User user;
    private LoanApplication application;
    private String action;
    private String details;
    private LocalDateTime actionTime;

    public AuditLog(){

    }
    public AuditLog(Integer logId, User user, LoanApplication application, String action, String details,
            LocalDateTime actionTime) {
        this.logId = logId;
        this.user = user;
        this.application = application;
        this.action = action;
        this.details = details;
        this.actionTime = actionTime;
    }
    public Integer getLogId() {
        return logId;
    }
    public void setLogId(Integer logId) {
        this.logId = logId;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public LoanApplication getApplication() {
        return application;
    }
    public void setApplication(LoanApplication application) {
        this.application = application;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public LocalDateTime getActionTime() {
        return actionTime;
    }
    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }
}
