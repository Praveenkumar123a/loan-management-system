package com.nexturn.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.AuditLog;
@Repository 
public interface AuditLogRepository extends JpaRepository<AuditLog,Integer>{
    
}
