package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.ApplicantProfile;

public interface ApplicantProfileService {

    ApplicantProfile createProfile(ApplicantProfile profile);

    List<ApplicantProfile> getAllProfiles();

    ApplicantProfile getProfileById(Integer profileId);

    ApplicantProfile updateProfile(
            Integer profileId,
            ApplicantProfile profile);

    void deleteProfile(Integer profileId);
}