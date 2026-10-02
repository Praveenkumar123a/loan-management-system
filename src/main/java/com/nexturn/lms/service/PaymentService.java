package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.Payment;

public interface PaymentService {
    Payment makePayment(Payment payment);

    Payment getPaymentById(Integer paymentId);

    List<Payment> getPaymentsByEmi(Integer emiId);
}
