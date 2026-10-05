package com.nexturn.lms.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    List<Payment> findByEmiSchedule_EmiId(Integer emiId);

    Optional<Payment> findByTransactionRef(String transactionRef);

    @Query("SELECT COALESCE(SUM(p.amountPaid), 0) " +
            "FROM Payment p WHERE p.emiSchedule.emiId = :emiId")
    BigDecimal getTotalAmountPaidForEmi(@Param("emiId") Integer emiId);
}