package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.LoanProduct;

public interface LoanProductService {
	LoanProduct saveLoanProduct(LoanProduct loanProduct);
	List<LoanProduct> getAllLoanProducts();
	LoanProduct getLoanProductById(Integer productId);
	LoanProduct updateLoanProduct(Integer productId,LoanProduct loanProduct);
	void deleteLoanProduct(Integer productId);

}
