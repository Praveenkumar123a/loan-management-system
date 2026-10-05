package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.LoanProduct;

public interface LoanProductService {
    LoanProduct create(LoanProduct product);
    List<LoanProduct> getAllActive();
    LoanProduct getById(Integer productId);
}