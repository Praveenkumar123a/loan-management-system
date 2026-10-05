package com.nexturn.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexturn.lms.dto.PayRequest;
import com.nexturn.lms.dto.PaymentResponse;
import com.nexturn.lms.entity.Payment;
import com.nexturn.lms.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/emi/{emiId}")
    public ResponseEntity<PaymentResponse> payEmi(
            @PathVariable Integer emiId,
            @RequestBody PayRequest request) {

        Payment payment = paymentService.payEmi(emiId, request.getPaymentMode(), request.getTransactionRef());
        return ResponseEntity.ok(new PaymentResponse(payment));
    }
}