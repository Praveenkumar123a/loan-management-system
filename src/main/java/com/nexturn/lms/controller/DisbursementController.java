package com.nexturn.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexturn.lms.dto.DisburseRequest;
import com.nexturn.lms.dto.DisbursementResponse;
import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
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

    @PostMapping("/application/{applicationId}")
    public ResponseEntity<DisbursementResponse> disburse(@PathVariable Integer applicationId,@RequestBody DisburseRequest request) {

        LoanApplication application = applicationService.getById(applicationId);

        User disbursedBy = userService.getById(request.getDisbursedById());

        Disbursement disbursement = disbursementService.disburse(application,disbursedBy,request.getDisbursedDate());

        return ResponseEntity.ok(new DisbursementResponse(disbursement));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<DisbursementResponse> getByApplication(@PathVariable Integer applicationId) {

        LoanApplication application = applicationService.getById(applicationId);

        Disbursement disbursement = disbursementService.getByApplication(application);

        return ResponseEntity.ok(new DisbursementResponse(disbursement));
    }
}