package com.nexturn.lms.service;

import com.nexturn.lms.entity.Payment;

public interface PaymentService {
    Payment payEmi(Integer emiId, String paymentMode, String transactionRef);
}