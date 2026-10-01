package com.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loan.entity.LoanProduct;

@Repository 
public interface LoanProductRepository extends JpaRepository<LoanProduct,Integer> {

}
