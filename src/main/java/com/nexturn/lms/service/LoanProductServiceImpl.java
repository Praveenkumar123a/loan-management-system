package com.nexturn.lms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.LoanProductRepository;


@Service
public class LoanProductServiceImpl implements LoanProductService {

    @Autowired
    private LoanProductRepository productRepository;

    @Override
    public LoanProduct create(LoanProduct product) {
        if (product.getIsActive() == null) {
            product.setIsActive(true);
        }
        return productRepository.save(product);
    }

    @Override
    public List<LoanProduct> getAllActive() {
        return productRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getIsActive()))
                .toList();
    }

    @Override
    public LoanProduct getById(Integer productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan product not found: " + productId));
    }
}