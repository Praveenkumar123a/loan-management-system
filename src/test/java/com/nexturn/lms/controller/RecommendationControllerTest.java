package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.Recommendation;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.LoanApplicationService;
import com.nexturn.lms.service.RecommendationService;
import com.nexturn.lms.service.UserService;

@ExtendWith(MockitoExtension.class)
class RecommendationControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @Mock
    private LoanApplicationService applicationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecommendationController recommendationController;

    private MockMvc mockMvc;

    private LoanApplication application;
    private User officer;
    private Recommendation recommendation;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(recommendationController)
                .build();

        application = new LoanApplication();
        application.setApplicationId(1);

        officer = new User();
        officer.setUserId(2);

        recommendation = new Recommendation();
        recommendation.setApplication(application);
        recommendation.setOfficer(officer);
        recommendation.setDecision("RECOMMEND");
        recommendation.setComments("Application meets eligibility criteria");
    }

    @Test
    void recommend_shouldReturnRecommendation() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        when(userService.getById(2))
                .thenReturn(officer);

        when(recommendationService.recommend(
                application,
                officer,
                true,
                "Application meets eligibility criteria"
        )).thenReturn(recommendation);

        String requestBody = """
                {
                    "officerId": 2,
                    "recommend": true,
                    "comments": "Application meets eligibility criteria"
                }
                """;

        mockMvc.perform(
                post("/api/recommendations/application/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getById(1);

        verify(userService)
                .getById(2);

        verify(recommendationService)
                .recommend(
                        application,
                        officer,
                        true,
                        "Application meets eligibility criteria"
                );
    }

    @Test
    void getByApplication_shouldReturnRecommendation() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        when(recommendationService.getByApplication(application))
                .thenReturn(recommendation);

        mockMvc.perform(
                get("/api/recommendations/application/1")
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getById(1);

        verify(recommendationService)
                .getByApplication(application);
    }
}