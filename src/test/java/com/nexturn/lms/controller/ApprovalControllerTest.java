package com.nexturn.lms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nexturn.lms.entity.Approval;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.ApprovalService;
import com.nexturn.lms.service.LoanApplicationService;
import com.nexturn.lms.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ApprovalControllerTest {

    @Mock
    private ApprovalService approvalService;

    @Mock
    private LoanApplicationService applicationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ApprovalController approvalController;

    private MockMvc mockMvc;

    private LoanApplication application;
    private User manager;
    private Approval approval;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(approvalController)
                .build();

        application = new LoanApplication();
        application.setApplicationId(1);

        manager = new User();
        manager.setUserId(2);

        approval = new Approval();
        approval.setApplication(application);
        approval.setManager(manager);
        approval.setDecision("APPROVED");
        approval.setComments("Loan approved");
        approval.setApprovedAmount(
                new BigDecimal("100000")
        );
        approval.setInterestRate(
                new BigDecimal("8.50")
        );
        approval.setTenureMonths(24);
    }

    @Test
    void decide_shouldApproveApplication() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        when(userService.getById(2))
                .thenReturn(manager);

        when(approvalService.decide(
                any(LoanApplication.class),
                any(User.class),
                any(Boolean.class),
                any(String.class),
                any(BigDecimal.class),
                any(BigDecimal.class),
                any(Integer.class)
        )).thenReturn(approval);

        String requestBody = """
                {
                    "managerId": 2,
                    "approve": true,
                    "comments": "Loan approved",
                    "approvedAmount": 100000,
                    "interestRate": 8.50,
                    "tenureMonths": 24
                }
                """;

        mockMvc.perform(
                post("/api/approvals/application/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getById(1);

        verify(userService)
                .getById(2);

        verify(approvalService)
                .decide(
                        application,
                        manager,
                        true,
                        "Loan approved",
                        new BigDecimal("100000"),
                        new BigDecimal("8.50"),
                        24
                );
    }

    @Test
    void decide_shouldRejectApplication() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        when(userService.getById(2))
                .thenReturn(manager);

        Approval rejectedApproval = new Approval();

        rejectedApproval.setApplication(application);
        rejectedApproval.setManager(manager);
        rejectedApproval.setDecision("REJECTED");
        rejectedApproval.setComments("Income criteria not satisfied");

        when(approvalService.decide(
                any(LoanApplication.class),
                any(User.class),
                any(Boolean.class),
                any(String.class),
                any(BigDecimal.class),
                any(BigDecimal.class),
                any(Integer.class)
        )).thenReturn(rejectedApproval);

        String requestBody = """
                {
                    "managerId": 2,
                    "approve": false,
                    "comments": "Income criteria not satisfied",
                    "approvedAmount": 100000,
                    "interestRate": 8.50,
                    "tenureMonths": 24
                }
                """;

        mockMvc.perform(
                post("/api/approvals/application/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(approvalService)
                .decide(
                        application,
                        manager,
                        false,
                        "Income criteria not satisfied",
                        new BigDecimal("100000"),
                        new BigDecimal("8.50"),
                        24
                );
    }

    @Test
    void getByApplication_shouldReturnApproval() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        when(approvalService.getByApplication(application))
                .thenReturn(approval);

        mockMvc.perform(
                get("/api/approvals/application/1")
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getById(1);

        verify(approvalService)
                .getByApplication(application);
    }
}