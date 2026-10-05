package com.nexturn.lms.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.ApplicantProfileRepository;

@Service
public class ApplicantProfileServiceImpl implements ApplicantProfileService {

    @Autowired
    private ApplicantProfileRepository profileRepository;

    @Override
    public ApplicantProfile createOrUpdateProfile(User user, LocalDate dob, String address, String pan,
                                                   String aadhaar, String employmentType,
                                                   BigDecimal monthlyIncome, BigDecimal existingLiabilities) {

        ApplicantProfile profile = profileRepository.findByUser(user).orElse(new ApplicantProfile());
        profile.setUser(user);
        profile.setDateOfBirth(dob);
        profile.setAddress(address);
        profile.setPanNumber(pan);
        profile.setAadhaarNumber(aadhaar);
        profile.setEmploymentType(employmentType);
        profile.setMonthlyIncome(monthlyIncome);
        profile.setExistingLiabilities(existingLiabilities == null ? BigDecimal.ZERO : existingLiabilities);

        return profileRepository.save(profile);
    }

    @Override
    public ApplicantProfile getByUser(User user) {
        return profileRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for user: " + user.getUserId()));
    }
}