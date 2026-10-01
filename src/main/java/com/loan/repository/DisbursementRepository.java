package com.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loan.entity.Disbursement;

@Repository 
public interface DisbursementRepository extends JpaRepository<Disbursement,Integer> {

}
