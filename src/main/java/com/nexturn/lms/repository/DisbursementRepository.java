package com.nexturn.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.LoanApplication;

@Repository
public interface DisbursementRepository extends JpaRepository<Disbursement, Integer> {

    Optional<Disbursement> findByApplication(LoanApplication application);
}