package com.nexturn.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.Approval;
import com.nexturn.lms.entity.LoanApplication;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval, Integer> {

    Optional<Approval> findByApplication(LoanApplication application);
}