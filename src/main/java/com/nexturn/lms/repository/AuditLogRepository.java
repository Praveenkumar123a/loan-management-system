package com.nexturn.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.AuditLog;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Integer> {

    List<AuditLog> findByUser(User user);
    List<AuditLog> findByApplication(LoanApplication application);
}