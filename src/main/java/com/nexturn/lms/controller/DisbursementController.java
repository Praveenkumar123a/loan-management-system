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

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.service.DisbursementService;

@RestController
@RequestMapping("/api/disbursements")
public class DisbursementController {

    private final DisbursementService disbursementService;

    public DisbursementController(
            DisbursementService disbursementService) {

        this.disbursementService = disbursementService;
    }

    @PostMapping
    public ResponseEntity<Disbursement> createDisbursement(
            @RequestBody Disbursement disbursement) {

        Disbursement savedDisbursement =
                disbursementService.createDisbursement(disbursement);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedDisbursement);
    }

    @GetMapping
    public ResponseEntity<List<Disbursement>> getAllDisbursements() {

        return ResponseEntity.ok(
                disbursementService.getAllDisbursements());
    }

    @GetMapping("/{disbursementId}")
    public ResponseEntity<Disbursement> getDisbursementById(
            @PathVariable Integer disbursementId) {

        return ResponseEntity.ok(
                disbursementService.getDisbursementById(disbursementId));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<Disbursement> getDisbursementByApplication(
            @PathVariable Integer applicationId) {

        return ResponseEntity.ok(
                disbursementService.getDisbursementByApplication(
                        applicationId));
    }

    @PutMapping("/{disbursementId}")
    public ResponseEntity<Disbursement> updateDisbursement(
            @PathVariable Integer disbursementId,
            @RequestBody Disbursement disbursement) {

        return ResponseEntity.ok(
                disbursementService.updateDisbursement(
                        disbursementId,
                        disbursement));
    }

    @DeleteMapping("/{disbursementId}")
    public ResponseEntity<Void> deleteDisbursement(
            @PathVariable Integer disbursementId) {

        disbursementService.deleteDisbursement(disbursementId);

        return ResponseEntity.noContent().build();
    }
}