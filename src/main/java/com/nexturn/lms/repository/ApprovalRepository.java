package com.nexturn.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.Approval;

@Repository 
public interface ApprovalRepository extends JpaRepository<Approval,Integer> {
    
}
