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
import com.nexturn.lms.entity.LoanProduct;
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
    private LoanApplicationController loanApplicationController;

    private MockMvc mockMvc;

    private User applicant;
    private LoanProduct product;
    private LoanApplication application;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(loanApplicationController)
                .build();

        applicant = new User();
        applicant.setUserId(1);

        product = new LoanProduct();
        product.setProductId(1);

        application = new LoanApplication();
        application.setApplicationId(1);
        application.setApplicant(applicant);
        application.setProduct(product);
    }

    @Test
    void submit_shouldReturnApplication() throws Exception {

        when(userService.getById(1))
                .thenReturn(applicant);

        when(applicationService.submitApplication(
                org.mockito.ArgumentMatchers.eq(applicant),
                org.mockito.ArgumentMatchers.eq(1),
                org.mockito.ArgumentMatchers.eq(new BigDecimal("500000")),
                org.mockito.ArgumentMatchers.eq(60),
                org.mockito.ArgumentMatchers.eq("Home renovation")
        )).thenReturn(application);

        String requestBody = """
                {
                    "productId": 1,
                    "requestedAmount": 500000,
                    "tenureMonths": 60,
                    "purpose": "Home renovation"
                }
                """;

        mockMvc.perform(
                post("/api/applications/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(userService).getById(1);

        verify(applicationService).submitApplication(
                org.mockito.ArgumentMatchers.eq(applicant),
                org.mockito.ArgumentMatchers.eq(1),
                org.mockito.ArgumentMatchers.eq(new BigDecimal("500000")),
                org.mockito.ArgumentMatchers.eq(60),
                org.mockito.ArgumentMatchers.eq("Home renovation")
        );
    }

    @Test
    void getById_shouldReturnApplication() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        mockMvc.perform(
                get("/api/applications/1")
        )
        .andExpect(status().isOk());

        verify(applicationService).getById(1);
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

        ApplicationStatus status = ApplicationStatus.SUBMITTED;

        application.setStatus(status);

        when(applicationService.getByStatus(status))
                .thenReturn(List.of(application));

        mockMvc.perform(
                get("/api/applications/status/SUBMITTED")
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getByStatus(status);
    }
}