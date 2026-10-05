package com.nexturn.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexturn.lms.dto.RecommendRequest;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.Recommendation;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.LoanApplicationService;
import com.nexturn.lms.service.RecommendationService;
import com.nexturn.lms.service.UserService;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "http://localhost:3000")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private LoanApplicationService applicationService;

    @Autowired
    private UserService userService;

    
    @PostMapping("/application/{applicationId}")
    public ResponseEntity<Recommendation> recommend(
            @PathVariable Integer applicationId,
            @RequestBody RecommendRequest request) {

        LoanApplication application = applicationService.getById(applicationId);
        User officer = userService.getById(request.getOfficerId());

        Recommendation recommendation = recommendationService.recommend(
                application, officer, request.isRecommend(), request.getComments());

        return ResponseEntity.ok(recommendation);
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<Recommendation> getByApplication(@PathVariable Integer applicationId) {
        LoanApplication application = applicationService.getById(applicationId);
        return ResponseEntity.ok(recommendationService.getByApplication(application));
    }
}