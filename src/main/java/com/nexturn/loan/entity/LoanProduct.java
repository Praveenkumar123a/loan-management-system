package com.nexturn.loan.entity;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
@Entity
@Table(name = "loan_products")
public class LoanProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;
	@Column(name = "min_amount",precision = 12,scale = 2,nullable = false)
    private BigDecimal minAmount;
	@Column(name = "max_amount",precision = 12,scale = 2,nullable = false)
    private BigDecimal maxAmount;
	@Column(name = "min_tenure_months",nullable = false)
	private int minTenureMonths;
	@Column(name = "max_tenure_months",nullable = false)
	private int maxTenureMonths;
	@Column(name = "default_interest_rate",precision = 5,scale = 2,nullable = false)
	private BigDecimal defaultInterestRate;
	@Column(name = "penalty_type",length = 10,nullable = false)
	private String penaltyType;
	@Column(name = "penalty_value",precision = 8,scale = 2,nullable = false)
	private BigDecimal penaltyValue;
	@Column(name = "is_active")
	private Boolean isActive;
	
	public LoanProduct() {
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public BigDecimal getMinAmount() {
		return minAmount;
	}
	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}
	public BigDecimal getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}
	public int getMinTenureMonths() {
		return minTenureMonths;
	}
	public void setMinTenureMonths(int minTenureMonths) {
		this.minTenureMonths = minTenureMonths;
	}
	public int getMaxTenureMonths() {
		return maxTenureMonths;
	}
	public void setMaxTenureMonths(int maxTenureMonths) {
		this.maxTenureMonths = maxTenureMonths;
	}
	public BigDecimal getDefaultInterestRate() {
		return defaultInterestRate;
	}
	public void setDefaultInterestRate(BigDecimal defaultInterestRate) {
		this.defaultInterestRate = defaultInterestRate;
	}
	public String getPenaltyType() {
		return penaltyType;
	}
	public void setPenaltyType(String penaltyType) {
		this.penaltyType = penaltyType;
	}
	public BigDecimal getPenaltyValue() {
		return penaltyValue;
	}
	public void setPenaltyValue(BigDecimal penaltyValue) {
		this.penaltyValue = penaltyValue;
	}
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
}
