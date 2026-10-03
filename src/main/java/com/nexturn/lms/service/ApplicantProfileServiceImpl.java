package com.nexturn.lms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.exception.ApplicantProfileNotFoundException;
import com.nexturn.lms.repository.ApplicantProfileRepository;

@Service
public class ApplicantProfileServiceImpl implements ApplicantProfileService {

    @Autowired
    ApplicantProfileRepository repo;

    @Override
    public String addApplicantProfile(ApplicantProfile profile) {
        ApplicantProfile ap = repo.save(profile);
        String str = "Applicant profile inserted " + ap.getProfileId();
        return str;
    }

    @Override
    public String updateApplicantProfile(ApplicantProfile profile) {
        repo.save(profile);
        String str = "Applicant profile updated";
        return str;
    }

    @Override
    public String removeApplicantProfile(Long profileId) {
        repo.deleteById(profileId);
        return "Applicant profile deleted";
    }

    @Override
    public List<ApplicantProfile> findAllApplicantProfiles() {
        return repo.findAll();
    }

    @Override
    public ApplicantProfile findApplicantProfileById(Long profileId) {
        Optional<ApplicantProfile> profile = repo.findById(profileId);

        if (profile.isEmpty())
            throw new ApplicantProfileNotFoundException();

        return profile.get();
    }
}