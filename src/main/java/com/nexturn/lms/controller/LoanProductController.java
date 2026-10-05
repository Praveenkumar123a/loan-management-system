package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.service.LoanProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000")
public class LoanProductController {

    @Autowired
    private LoanProductService productService;

    
    @PostMapping
    public ResponseEntity<LoanProduct> create(@RequestBody LoanProduct product) {
        return ResponseEntity.ok(productService.create(product));
    }

    @GetMapping
    public ResponseEntity<List<LoanProduct>> getAllActive() {
        return ResponseEntity.ok(productService.getAllActive());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<LoanProduct> getById(@PathVariable Integer productId) {
        return ResponseEntity.ok(productService.getById(productId));
    }
}