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

import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.service.LoanApplicationService;

@RestController
@RequestMapping("/api/applications")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(
            LoanApplicationService loanApplicationService) {

        this.loanApplicationService = loanApplicationService;
    }

    @PostMapping
    public ResponseEntity<LoanApplication> createApplication(
            @RequestBody LoanApplication application) {

        LoanApplication savedApplication =
                loanApplicationService.createApplication(application);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedApplication);
    }

    @GetMapping
    public ResponseEntity<List<LoanApplication>> getAllApplications() {

        return ResponseEntity.ok(
                loanApplicationService.getAllApplications());
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<LoanApplication> getApplicationById(
            @PathVariable Integer applicationId) {

        return ResponseEntity.ok(
                loanApplicationService.getApplicationById(applicationId));
    }

    @PutMapping("/{applicationId}")
    public ResponseEntity<LoanApplication> updateApplication(
            @PathVariable Integer applicationId,
            @RequestBody LoanApplication application) {

        return ResponseEntity.ok(
                loanApplicationService.updateApplication(
                        applicationId,
                        application));
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Integer applicationId) {

        loanApplicationService.deleteApplication(applicationId);

        return ResponseEntity.noContent().build();
    }
}