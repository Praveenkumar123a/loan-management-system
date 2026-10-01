package com.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog,Integer>{
    
}
