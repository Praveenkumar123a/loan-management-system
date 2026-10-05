package com.nexturn.lms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nexturn.lms.entity.ApplicantProfile;

public class ProfileResponse {
    private Integer profileId;
    private Integer userId;
    private LocalDate dateOfBirth;
    private String address;
    private String panNumber;
    private String aadhaarNumber;
    private String employmentType;
    private BigDecimal monthlyIncome;
    private BigDecimal existingLiabilities;

    public ProfileResponse(ApplicantProfile profile) {
        this.profileId = profile.getProfileId();
        this.userId = profile.getUser().getUserId();
        this.dateOfBirth = profile.getDateOfBirth();
        this.address = profile.getAddress();
        this.panNumber = profile.getPanNumber();
        this.aadhaarNumber = profile.getAadhaarNumber();
        this.employmentType = profile.getEmploymentType();
        this.monthlyIncome = profile.getMonthlyIncome();
        this.existingLiabilities = profile.getExistingLiabilities();
    }

    public Integer getProfileId() { return profileId; }
    public Integer getUserId() { return userId; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getAddress() { return address; }
    public String getPanNumber() { return panNumber; }
    public String getAadhaarNumber() { return aadhaarNumber; }
    public String getEmploymentType() { return employmentType; }
    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public BigDecimal getExistingLiabilities() { return existingLiabilities; }
}