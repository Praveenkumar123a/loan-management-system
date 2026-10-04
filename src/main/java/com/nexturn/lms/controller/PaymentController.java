package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexturn.lms.entity.Payment;
import com.nexturn.lms.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<Payment> makePayment(
            @RequestBody Payment payment) {

        Payment savedPayment =
                paymentService.makePayment(payment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedPayment);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(
            @PathVariable Integer paymentId) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/emi/{emiId}")
    public ResponseEntity<List<Payment>> getPaymentsByEmi(
            @PathVariable Integer emiId) {

        return ResponseEntity.ok(
                paymentService.getPaymentsByEmi(emiId));
    }
}