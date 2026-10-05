package com.nexturn.lms.service;

import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.Recommendation;
import com.nexturn.lms.entity.User;

public interface RecommendationService {
    Recommendation recommend(LoanApplication application, User officer, boolean recommend, String comments);
    Recommendation getByApplication(LoanApplication application);
}