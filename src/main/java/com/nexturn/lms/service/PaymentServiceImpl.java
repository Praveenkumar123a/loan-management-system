package com.nexturn.lms.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.entity.Payment;
import com.nexturn.lms.repository.EmiScheduleRepository;

import com.nexturn.lms.repository.PaymentRepository;

import com.nexturn.lms.utils.EmiStatus;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmiScheduleRepository emiScheduleRepository;

    @Autowired
    private EmiScheduleService emiScheduleService;

    @Override
    public Payment payEmi(Integer emiId, String paymentMode, String transactionRef) {

        EmiSchedule emi = emiScheduleService.getById(emiId);
        Disbursement disbursement = emi.getDisbursement();
        LoanProduct product = disbursement.getApplication().getProduct();

        BigDecimal penalty = BigDecimal.ZERO;
        LocalDate today = LocalDate.now();

        if (today.isAfter(emi.getDueDate())) {
            penalty = calculatePenalty(emi, product, today);
        }

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(emi.getEmiAmount());
        payment.setPenaltyPaid(penalty);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMode(paymentMode);
        payment.setTransactionRef(transactionRef);

        Payment savedPayment = paymentRepository.save(payment);

        emi.setPenaltyAmount(penalty);
        emi.setStatus(EmiStatus.PAID);
        emi.setPaidDate(today);
        emiScheduleRepository.save(emi);

        return savedPayment;
    }

    private BigDecimal calculatePenalty(EmiSchedule emi, LoanProduct product, LocalDate today) {
        if ("PERCENT".equals(product.getPenaltyType())) {
            return emi.getEmiAmount()
                    .multiply(product.getPenaltyValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else { 
            return product.getPenaltyValue();
        }
    }
}