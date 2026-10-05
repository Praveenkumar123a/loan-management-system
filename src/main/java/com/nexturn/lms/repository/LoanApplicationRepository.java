package com.nexturn.lms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.utils.ApplicationStatus;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication,Integer> {
    List<LoanApplication> findByApplicant_UserId(Integer userId);
    List<LoanApplication> findByStatus(ApplicationStatus status);
}