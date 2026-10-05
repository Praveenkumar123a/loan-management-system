package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

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
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.LoanApplicationService;
import com.nexturn.lms.service.UserService;
import com.nexturn.lms.utils.ApplicationStatus;

@ExtendWith(MockitoExtension.class)
class LoanApplicationControllerTest {

    @Mock
    private LoanApplicationService applicationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoanApplicationController applicationController;

    private MockMvc mockMvc;

    private User applicant;
    private LoanApplication application;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(applicationController)
                .build();

        applicant = new User();
        applicant.setUserId(1);

        application = new LoanApplication();
        application.setApplicationId(10);
    }

    @Test
    void submit_shouldReturnApplication() throws Exception {

        when(userService.getById(1))
                .thenReturn(applicant);

        when(applicationService.submitApplication(
                applicant,
                5,
                new BigDecimal("100000"),
                24,
                "Home renovation"
        )).thenReturn(application);

        String requestBody = """
                {
                    "productId": 5,
                    "requestedAmount": 100000,
                    "tenureMonths": 24,
                    "purpose": "Home renovation"
                }
                """;

        mockMvc.perform(
                post("/api/applications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(userService)
                .getById(1);

        verify(applicationService)
                .submitApplication(
                        applicant,
                        5,
                        new BigDecimal("100000"),
                        24,
                        "Home renovation"
                );
    }

    @Test
    void getById_shouldReturnApplication() throws Exception {

        when(applicationService.getById(10))
                .thenReturn(application);

        mockMvc.perform(
                get("/api/applications/10")
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getById(10);
    }

    @Test
    void getByApplicant_shouldReturnApplications() throws Exception {

        when(applicationService.getByApplicant(1))
                .thenReturn(List.of(application));

        mockMvc.perform(
                get("/api/applications/applicant/1")
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getByApplicant(1);
    }

    @Test
    void getByStatus_shouldReturnApplications() throws Exception {

        when(applicationService.getByStatus(ApplicationStatus.SUBMITTED))
                .thenReturn(List.of(application));

        mockMvc.perform(
                get("/api/applications/status/SUBMITTED")
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getByStatus(ApplicationStatus.SUBMITTED);
    }
}