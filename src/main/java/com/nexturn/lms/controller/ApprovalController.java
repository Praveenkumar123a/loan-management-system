package com.nexturn.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexturn.lms.dto.ApprovalRequest;
import com.nexturn.lms.dto.ApprovalResponse;
import com.nexturn.lms.entity.Approval;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.ApprovalService;
import com.nexturn.lms.service.LoanApplicationService;
import com.nexturn.lms.service.UserService;

@RestController
@RequestMapping("/api/approvals")
@CrossOrigin(origins = "http://localhost:3000")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private LoanApplicationService applicationService;

    @Autowired
    private UserService userService;

    @PostMapping("/application/{applicationId}")
    public ResponseEntity<ApprovalResponse> decide(
            @PathVariable Integer applicationId,
            @RequestBody ApprovalRequest request) {

        LoanApplication application = applicationService.getById(applicationId);
        User manager = userService.getById(request.getManagerId());

        Approval approval = approvalService.decide(
                application, manager, request.isApprove(), request.getComments(),
                request.getApprovedAmount(), request.getInterestRate(), request.getTenureMonths());

        return ResponseEntity.ok(new ApprovalResponse(approval));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<ApprovalResponse> getByApplication(@PathVariable Integer applicationId) {
        LoanApplication application = applicationService.getById(applicationId);
        Approval approval = approvalService.getByApplication(application);
        return ResponseEntity.ok(new ApprovalResponse(approval));
    }
}