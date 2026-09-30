package com.loan.repository;

<<<<<<< HEAD
public interface PaymentRepository {

=======
import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    
>>>>>>> feature/payment
}
