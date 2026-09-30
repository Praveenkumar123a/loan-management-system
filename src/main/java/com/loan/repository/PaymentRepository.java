package com.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
    
}
