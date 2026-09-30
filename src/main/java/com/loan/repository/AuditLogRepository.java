package com.loan.repository;

<<<<<<< HEAD
public interface AuditLogRepository {

=======
import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog,Integer>{
    
>>>>>>> feature/payment
}
