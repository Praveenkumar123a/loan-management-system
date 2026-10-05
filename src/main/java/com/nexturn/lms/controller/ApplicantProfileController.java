package com.nexturn.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexturn.lms.dto.ProfileRequest;
import com.nexturn.lms.dto.ProfileResponse;
import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.ApplicantProfileService;
import com.nexturn.lms.service.UserService;

@RestController
@RequestMapping("/api/applicant/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class ApplicantProfileController {

    @Autowired
    private ApplicantProfileService profileService;

    @Autowired
    private UserService userService;

    @PostMapping("/{userId}")
    public ResponseEntity<ProfileResponse> createOrUpdate(
            @PathVariable Integer userId,
            @RequestBody ProfileRequest request) {

        User user = userService.getById(userId);
        ApplicantProfile profile = profileService.createOrUpdateProfile(
                user, request.getDateOfBirth(), request.getAddress(), request.getPanNumber(),
                request.getAadhaarNumber(), request.getEmploymentType(),
                request.getMonthlyIncome(), request.getExistingLiabilities());

        return ResponseEntity.ok(new ProfileResponse(profile));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResponse> getByUser(@PathVariable Integer userId) {
        User user = userService.getById(userId);
        ApplicantProfile profile = profileService.getByUser(user);
        return ResponseEntity.ok(new ProfileResponse(profile));
    }
}