package com.nexturn.lms.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.User;

public interface ApplicantProfileService {
    ApplicantProfile createOrUpdateProfile(User user, LocalDate dob, String address, String pan,
                                            String aadhaar, String employmentType,
                                            BigDecimal monthlyIncome, BigDecimal existingLiabilities);
    ApplicantProfile getByUser(User user);
}