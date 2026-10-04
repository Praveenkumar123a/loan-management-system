package com.nexturn.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.exception.ApplicantProfileNotFoundException;
import com.nexturn.lms.repository.ApplicantProfileRepository;

@Service
public class ApplicantProfileServiceImpl implements ApplicantProfileService {

    private final ApplicantProfileRepository applicantProfileRepository;

    public ApplicantProfileServiceImpl(
            ApplicantProfileRepository applicantProfileRepository) {

        this.applicantProfileRepository = applicantProfileRepository;
    }

    @Override
    public ApplicantProfile createProfile(
            ApplicantProfile profile) {

        if (profile == null) {
            throw new IllegalArgumentException(
                    "Applicant profile cannot be null");
        }

        return applicantProfileRepository.save(profile);
    }

    @Override
    public List<ApplicantProfile> getAllProfiles() {

        return applicantProfileRepository.findAll();
    }

    @Override
    public ApplicantProfile getProfileById(
            Integer profileId) {

        return applicantProfileRepository.findById(profileId)
                .orElseThrow(() ->
                        new ApplicantProfileNotFoundException(
                                "Applicant profile not found"));
    }

    @Override
    public ApplicantProfile updateProfile(
            Integer profileId,
            ApplicantProfile profile) {

        ApplicantProfile existingProfile =
                applicantProfileRepository.findById(profileId)
                        .orElseThrow(() ->
                                new ApplicantProfileNotFoundException(
                                        "Applicant profile not found"));

        existingProfile.setUser(profile.getUser());
        existingProfile.setDateOfBirth(
                profile.getDateOfBirth());
        existingProfile.setAddress(
                profile.getAddress());
        existingProfile.setPanNumber(
                profile.getPanNumber());
        existingProfile.setAadhaarNumber(
                profile.getAadhaarNumber());
        existingProfile.setEmploymentType(
                profile.getEmploymentType());
        existingProfile.setMonthlyIncome(
                profile.getMonthlyIncome());
        existingProfile.setExistingLiabilities(
                profile.getExistingLiabilities());

        return applicantProfileRepository.save(
                existingProfile);
    }

    @Override
    public void deleteProfile(Integer profileId) {

        ApplicantProfile existingProfile =
                applicantProfileRepository.findById(profileId)
                        .orElseThrow(() ->
                                new ApplicantProfileNotFoundException(
                                        "Applicant profile not found"));

        applicantProfileRepository.delete(existingProfile);
    }
}