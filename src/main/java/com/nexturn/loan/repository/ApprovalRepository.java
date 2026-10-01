package com.nexturn.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.loan.entity.Approval;

@Repository 
public interface ApprovalRepository extends JpaRepository<Approval,Integer> {
    
}
