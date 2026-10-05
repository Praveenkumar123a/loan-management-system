package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexturn.lms.dto.ApplicationRequest;
import com.nexturn.lms.dto.ApplicationResponse;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.LoanApplicationService;
import com.nexturn.lms.service.UserService;
import com.nexturn.lms.utils.ApplicationStatus;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:3000")
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService applicationService;

    @Autowired
    private UserService userService;

    @PostMapping("/{applicantId}")
    public ResponseEntity<ApplicationResponse> submit(
            @PathVariable Integer applicantId,
            @RequestBody ApplicationRequest request) {

        User applicant = userService.getById(applicantId);

        LoanApplication application = applicationService.submitApplication(
                applicant,
                request.getProductId(),
                request.getRequestedAmount(),
                request.getTenureMonths(),
                request.getPurpose());

        return ResponseEntity.ok(new ApplicationResponse(application));
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationResponse> getById(
            @PathVariable Integer applicationId) {

        LoanApplication application = applicationService.getById(applicationId);

        return ResponseEntity.ok(new ApplicationResponse(application));
    }

    @GetMapping("/applicant/{applicantId}")
    public ResponseEntity<List<ApplicationResponse>> getByApplicant(
            @PathVariable Integer applicantId) {

        List<ApplicationResponse> responses = applicationService
                .getByApplicant(applicantId)
                .stream()
                .map(ApplicationResponse::new)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ApplicationResponse>> getByStatus(
            @PathVariable ApplicationStatus status) {

        List<ApplicationResponse> responses = applicationService
                .getByStatus(status)
                .stream()
                .map(ApplicationResponse::new)
                .toList();

        return ResponseEntity.ok(responses);
    }
}