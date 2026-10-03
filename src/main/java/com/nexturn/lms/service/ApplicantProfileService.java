package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.ApplicantProfile;

public interface ApplicantProfileService {

    String addApplicantProfile(ApplicantProfile profile);
    String updateApplicantProfile(ApplicantProfile profile);
    String removeApplicantProfile(Long profileId);
    List<ApplicantProfile> findAllApplicantProfiles();
    ApplicantProfile findApplicantProfileById(Long profileId);
}