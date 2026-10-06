package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexturn.lms.dto.EmiScheduleResponse;
import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.service.DisbursementService;
import com.nexturn.lms.service.EmiScheduleService;

@RestController
@RequestMapping("/api/emi")
@CrossOrigin(origins = "http://localhost:3000")
public class EmiScheduleController {

    @Autowired
    private EmiScheduleService emiScheduleService;
    
    @Autowired
    private DisbursementService disbursementService;

    @GetMapping("/disbursement/{disbursementId}")
    public ResponseEntity<List<EmiScheduleResponse>> getByDisbursement(@PathVariable Integer disbursementId) {
    	Disbursement disbursement =disbursementService.getById(disbursementId);

        List<EmiScheduleResponse> responses = emiScheduleService.getByDisbursement(disbursement)
                .stream().map(EmiScheduleResponse::new).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<EmiScheduleResponse>> getOverdue() {
        List<EmiScheduleResponse> responses = emiScheduleService.getOverdueEmis()
                .stream().map(EmiScheduleResponse::new).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{emiId}")
    public ResponseEntity<EmiScheduleResponse> getById(@PathVariable Integer emiId) {
        return ResponseEntity.ok(new EmiScheduleResponse(emiScheduleService.getById(emiId)));
    }
}