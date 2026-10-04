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

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.service.ApplicantProfileService;

@RestController
@RequestMapping("/api/applicant-profiles")
public class ApplicantProfileController {

    private final ApplicantProfileService applicantProfileService;

    public ApplicantProfileController(
            ApplicantProfileService applicantProfileService) {

        this.applicantProfileService = applicantProfileService;
    }

    @PostMapping
    public ResponseEntity<ApplicantProfile> createProfile(
            @RequestBody ApplicantProfile profile) {

        ApplicantProfile savedProfile =
                applicantProfileService.createProfile(profile);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProfile);
    }

    @GetMapping
    public ResponseEntity<List<ApplicantProfile>> getAllProfiles() {

        return ResponseEntity.ok(
                applicantProfileService.getAllProfiles());
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ApplicantProfile> getProfileById(
            @PathVariable Integer profileId) {

        return ResponseEntity.ok(
                applicantProfileService.getProfileById(profileId));
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<ApplicantProfile> updateProfile(
            @PathVariable Integer profileId,
            @RequestBody ApplicantProfile profile) {

        return ResponseEntity.ok(
                applicantProfileService.updateProfile(
                        profileId,
                        profile));
    }

    @DeleteMapping("/{profileId}")
    public ResponseEntity<Void> deleteProfile(
            @PathVariable Integer profileId) {

        applicantProfileService.deleteProfile(profileId);

        return ResponseEntity.noContent().build();
    }
}