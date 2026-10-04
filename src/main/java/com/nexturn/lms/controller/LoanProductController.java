package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.service.LoanProductService;

@RestController
@RequestMapping("/api/loan-products")
public class LoanProductController {

    private final LoanProductService loanProductService;

    public LoanProductController(
            LoanProductService loanProductService) {

        this.loanProductService = loanProductService;
    }

    @PostMapping
    public ResponseEntity<LoanProduct> createLoanProduct(
            @RequestBody LoanProduct loanProduct) {

        LoanProduct savedLoanProduct =
                loanProductService.createLoanProduct(loanProduct);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedLoanProduct);
    }

    @GetMapping
    public ResponseEntity<List<LoanProduct>> getAllLoanProducts() {

        return ResponseEntity.ok(
                loanProductService.getAllLoanProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<LoanProduct> getLoanProductById(
            @PathVariable Integer productId) {

        return ResponseEntity.ok(
                loanProductService.getLoanProductById(productId));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<LoanProduct> updateLoanProduct(
            @PathVariable Integer productId,
            @RequestBody LoanProduct loanProduct) {

        return ResponseEntity.ok(
                loanProductService.updateLoanProduct(
                        productId,
                        loanProduct));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteLoanProduct(
            @PathVariable Integer productId) {

        loanProductService.deleteLoanProduct(productId);

        return ResponseEntity.noContent().build();
    }
}