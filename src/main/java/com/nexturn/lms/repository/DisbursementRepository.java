package com.nexturn.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.Disbursement;

@Repository 
public interface DisbursementRepository extends JpaRepository<Disbursement,Integer> {

}
