package com.nexturn.lms.service;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.LoanApplication;

public interface EligibilityRuleService {
    int calculateEligibilityScore(ApplicantProfile profile, LoanApplication application);
}