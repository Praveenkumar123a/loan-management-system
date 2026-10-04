package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexturn.lms.entity.Recommendation;
import com.nexturn.lms.service.RecommendationService;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService) {

        this.recommendationService = recommendationService;
    }

    @PostMapping
    public ResponseEntity<Recommendation> createRecommendation(
            @RequestBody Recommendation recommendation) {

        Recommendation savedRecommendation =
                recommendationService.createRecommendation(recommendation);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedRecommendation);
    }

    @GetMapping
    public ResponseEntity<List<Recommendation>> getAllRecommendations() {

        return ResponseEntity.ok(
                recommendationService.getAllRecommendations());
    }

    @GetMapping("/{recommendationId}")
    public ResponseEntity<Recommendation> getRecommendationById(
            @PathVariable Integer recommendationId) {

        return ResponseEntity.ok(
                recommendationService.getRecommendationById(
                        recommendationId));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsByApplication(
            @PathVariable Integer applicationId) {

        return ResponseEntity.ok(
                recommendationService.getRecommendationsByApplication(
                        applicationId));
    }

    @PutMapping("/{recommendationId}")
    public ResponseEntity<Recommendation> updateRecommendation(
            @PathVariable Integer recommendationId,
            @RequestBody Recommendation recommendation) {

        return ResponseEntity.ok(
                recommendationService.updateRecommendation(
                        recommendationId,
                        recommendation));
    }

    @DeleteMapping("/{recommendationId}")
    public ResponseEntity<Void> deleteRecommendation(
            @PathVariable Integer recommendationId) {

        recommendationService.deleteRecommendation(
                recommendationId);

        return ResponseEntity.noContent().build();
    }
}