package com.nexturn.lms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProfileRequest {
    private LocalDate dateOfBirth;
    private String address;
    private String panNumber;
    private String aadhaarNumber;
    private String employmentType;
    private BigDecimal monthlyIncome;
    private BigDecimal existingLiabilities;

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPanNumber() { return panNumber; }
    public void setPanNumber(String panNumber) { this.panNumber = panNumber; }
    public String getAadhaarNumber() { return aadhaarNumber; }
    public void setAadhaarNumber(String aadhaarNumber) { this.aadhaarNumber = aadhaarNumber; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
    public BigDecimal getMonthlyIncome() { return monthlyIncome; }
    public void setMonthlyIncome(BigDecimal monthlyIncome) { this.monthlyIncome = monthlyIncome; }
    public BigDecimal getExistingLiabilities() { return existingLiabilities; }
    public void setExistingLiabilities(BigDecimal existingLiabilities) { this.existingLiabilities = existingLiabilities; }
}