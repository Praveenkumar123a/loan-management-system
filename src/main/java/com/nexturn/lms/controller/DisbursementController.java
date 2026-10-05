package com.nexturn.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexturn.lms.dto.DisburseRequest;
import com.nexturn.lms.dto.DisbursementResponse;
import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.DisbursementRepository;
import com.nexturn.lms.service.DisbursementService;
import com.nexturn.lms.service.LoanApplicationService;
import com.nexturn.lms.service.UserService;

@RestController
@RequestMapping("/api/disbursements")
@CrossOrigin(origins = "http://localhost:3000")
public class DisbursementController {

    @Autowired
    private DisbursementService disbursementService;

    @Autowired
    private LoanApplicationService applicationService;

    @Autowired
    private UserService userService;

    @Autowired
    private DisbursementRepository disbursementRepository;

    @PostMapping("/application/{applicationId}")
    public ResponseEntity<DisbursementResponse> disburse(
            @PathVariable Integer applicationId,
            @RequestBody DisburseRequest request) {

        LoanApplication application = applicationService.getById(applicationId);
        User disbursedBy = userService.getById(request.getDisbursedById());

        Disbursement disbursement = disbursementService.disburse(
                application,
                disbursedBy,
                request.getDisbursedDate());

        return ResponseEntity.ok(new DisbursementResponse(disbursement));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<DisbursementResponse> getByApplication(
            @PathVariable Integer applicationId) {

        LoanApplication application = applicationService.getById(applicationId);

        Disbursement disbursement = disbursementRepository
                .findByApplication(application)
                .orElseThrow(() -> new RuntimeException(
                        "Disbursement not found for application: " + applicationId));

        return ResponseEntity.ok(new DisbursementResponse(disbursement));
    }
}