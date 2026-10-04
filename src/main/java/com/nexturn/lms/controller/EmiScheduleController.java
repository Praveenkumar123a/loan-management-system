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

import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.service.EmiScheduleService;

@RestController
@RequestMapping("/api/emi-schedules")
public class EmiScheduleController {

    private final EmiScheduleService emiScheduleService;

    public EmiScheduleController(
            EmiScheduleService emiScheduleService) {

        this.emiScheduleService = emiScheduleService;
    }

    @PostMapping
    public ResponseEntity<EmiSchedule> createEmiSchedule(
            @RequestBody EmiSchedule emiSchedule) {

        EmiSchedule savedEmiSchedule =
                emiScheduleService.createEmiSchedule(emiSchedule);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedEmiSchedule);
    }

    @GetMapping
    public ResponseEntity<List<EmiSchedule>> getAllEmiSchedules() {

        return ResponseEntity.ok(
                emiScheduleService.getAllEmiSchedules());
    }

    @GetMapping("/{emiId}")
    public ResponseEntity<EmiSchedule> getEmiScheduleById(
            @PathVariable Integer emiId) {

        return ResponseEntity.ok(
                emiScheduleService.getEmiScheduleById(emiId));
    }

    @GetMapping("/disbursement/{disbursementId}")
    public ResponseEntity<List<EmiSchedule>> getEmiSchedulesByDisbursement(
            @PathVariable Integer disbursementId) {

        return ResponseEntity.ok(
                emiScheduleService.getEmiSchedulesByDisbursement(
                        disbursementId));
    }

    @PutMapping("/{emiId}")
    public ResponseEntity<EmiSchedule> updateEmiSchedule(
            @PathVariable Integer emiId,
            @RequestBody EmiSchedule emiSchedule) {

        return ResponseEntity.ok(
                emiScheduleService.updateEmiSchedule(
                        emiId,
                        emiSchedule));
    }

    @DeleteMapping("/{emiId}")
    public ResponseEntity<Void> deleteEmiSchedule(
            @PathVariable Integer emiId) {

        emiScheduleService.deleteEmiSchedule(emiId);

        return ResponseEntity.noContent().build();
    }
}