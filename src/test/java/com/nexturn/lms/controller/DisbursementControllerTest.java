package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.DisbursementRepository;
import com.nexturn.lms.service.DisbursementService;
import com.nexturn.lms.service.LoanApplicationService;
import com.nexturn.lms.service.UserService;

@ExtendWith(MockitoExtension.class)
class DisbursementControllerTest {

    @Mock
    private DisbursementService disbursementService;

    @Mock
    private LoanApplicationService applicationService;

    @Mock
    private UserService userService;

    @Mock
    private DisbursementRepository disbursementRepository;

    @InjectMocks
    private DisbursementController disbursementController;

    private MockMvc mockMvc;

    private LoanApplication application;
    private User user;
    private Disbursement disbursement;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(disbursementController)
                .build();

        application = new LoanApplication();
        application.setApplicationId(1);

        user = new User();
        user.setUserId(2);

        disbursement = new Disbursement();
        disbursement.setApplication(application);
        disbursement.setDisbursedBy(user);
        disbursement.setDisbursedDate(LocalDate.of(2026, 10, 5));
    }

    @Test
    void disburse_shouldReturnDisbursement() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        when(userService.getById(2))
                .thenReturn(user);

        when(disbursementService.disburse(
                application,
                user,
                LocalDate.of(2026, 10, 5)
        )).thenReturn(disbursement);

        String requestBody = """
                {
                    "disbursedById": 2,
                    "disbursedDate": "2026-10-05"
                }
                """;

        mockMvc.perform(
                post("/api/disbursements/application/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getById(1);

        verify(userService)
                .getById(2);

        verify(disbursementService)
                .disburse(
                        application,
                        user,
                        LocalDate.of(2026, 10, 5)
                );
    }

    @Test
    void getByApplication_shouldReturnDisbursement() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        when(disbursementRepository.findByApplication(application))
                .thenReturn(Optional.of(disbursement));

        mockMvc.perform(
                get("/api/disbursements/application/1")
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getById(1);

        verify(disbursementRepository)
                .findByApplication(application);
    }
}