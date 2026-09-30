package com.loan.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="applicant_profiles")
public class ApplicantProfile {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int profileId;
	@OneToOne
	@JoinColumn(nullable=false,unique=true)
	private User user;
	@Column(nullable=false)
	private LocalDate dateOfBirth;
	@Column(length=255, nullable = false)
	private String address;
	@Column(length=10,unique=true)
	private String panNumber;
	@Column(length=12, unique=true)
	private String aadhaarNumber;
	@Column(length = 30, nullable = false)
	private String employmentType;
	@Column(nullable = false)
	private BigDecimal monthlyIncome;
	@Column
	private BigDecimal existingLiabilities;
	public int getProfileId() {
		return profileId;
	}
	public void setProfileId(int profileId) {
		this.profileId = profileId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getAadhaarNumber() {
		return aadhaarNumber;
	}
	public void setAadhaarNumber(String aadhaarNumber) {
		this.aadhaarNumber = aadhaarNumber;
	}
	public String getEmploymentType() {
		return employmentType;
	}
	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}
	public BigDecimal getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(BigDecimal monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public BigDecimal getExistingLiabilities() {
		return existingLiabilities;
	}
	public void setExistingLiabilities(BigDecimal existingLiabilities) {
		this.existingLiabilities = existingLiabilities;
	}
	
	
	
	

}
