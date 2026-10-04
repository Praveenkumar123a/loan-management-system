package com.nexturn.lms.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.repository.LoanProductRepository;
@Service
public class LoanProductServiceImpl implements LoanProductService {
	private final LoanProductRepository loanProductRepository;
	public LoanProductServiceImpl(LoanProductRepository loanProductRepository) {
		this.loanProductRepository = loanProductRepository;
	}
	@Override
	public LoanProduct saveLoanProduct(LoanProduct loanProduct) {
		return loanProductRepository.save(loanProduct);
	}
	@Override
	public List<LoanProduct> getAllLoanProducts() {
		return loanProductRepository.findAll();
	}
	@Override
	public LoanProduct getLoanProductById(Integer productId) {
		return loanProductRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Loan product not found"));
	}
	@Override
	public LoanProduct updateLoanProduct(Integer productId, LoanProduct loanProduct) {
		LoanProduct existingProduct = loanProductRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Loan product not found"));
		existingProduct.setProductId(loanProduct.getProductId());
		existingProduct.setMinAmount(loanProduct.getMinAmount());
		existingProduct.setMaxAmount(loanProduct.getMaxAmount());;
		existingProduct.setMinTenureMonths(loanProduct.getMinTenureMonths());
		existingProduct.setMaxTenureMonths(loanProduct.getMinTenureMonths());
		existingProduct.setDefaultInterestRate(loanProduct.getDefaultInterestRate());
		existingProduct.setPenaltyType(loanProduct.getPenaltyType());
		existingProduct.setPenaltyValue(loanProduct.getPenaltyValue());
		existingProduct.setIsActive(loanProduct.getIsActive());
		return loanProductRepository.save(existingProduct);
	}
	@Override
	public void deleteLoanProduct(Integer productId) {
	      loanProductRepository.deleteById(productId);	
	}
}
