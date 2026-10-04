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

import com.nexturn.lms.entity.Approval;
import com.nexturn.lms.service.ApprovalService;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    private final ApprovalService approvalService;

    public ApprovalController(ApprovalService approvalService) {
        this.approvalService = approvalService;
    }

    @PostMapping
    public ResponseEntity<Approval> createApproval(
            @RequestBody Approval approval) {

        Approval savedApproval =
                approvalService.createApproval(approval);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedApproval);
    }

    @GetMapping
    public ResponseEntity<List<Approval>> getAllApprovals() {

        return ResponseEntity.ok(
                approvalService.getAllApprovals());
    }

    @GetMapping("/{approvalId}")
    public ResponseEntity<Approval> getApprovalById(
            @PathVariable Integer approvalId) {

        return ResponseEntity.ok(
                approvalService.getApprovalById(approvalId));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<Approval>> getApprovalsByApplication(
            @PathVariable Integer applicationId) {

        return ResponseEntity.ok(
                approvalService.getApprovalsByApplication(applicationId));
    }

    @PutMapping("/{approvalId}")
    public ResponseEntity<Approval> updateApproval(
            @PathVariable Integer approvalId,
            @RequestBody Approval approval) {

        return ResponseEntity.ok(
                approvalService.updateApproval(
                        approvalId,
                        approval));
    }

    @DeleteMapping("/{approvalId}")
    public ResponseEntity<Void> deleteApproval(
            @PathVariable Integer approvalId) {

        approvalService.deleteApproval(approvalId);

        return ResponseEntity.noContent().build();
    }
}