package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.repository.DisbursementRepository;
import com.nexturn.lms.service.EmiScheduleService;

@ExtendWith(MockitoExtension.class)
class EmiScheduleControllerTest {

    @Mock
    private EmiScheduleService emiScheduleService;

    @Mock
    private DisbursementRepository disbursementRepository;

    @InjectMocks
    private EmiScheduleController emiScheduleController;

    private MockMvc mockMvc;

    private Disbursement disbursement;
    private EmiSchedule emiSchedule;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(emiScheduleController)
                .build();

        disbursement = new Disbursement();
        disbursement.setDisbursementId(1);

        emiSchedule = new EmiSchedule();
        emiSchedule.setEmiId(1);
        emiSchedule.setInstallmentNo(1);
    }

    @Test
    void getByDisbursement_shouldReturnEmiSchedules() throws Exception {

        when(disbursementRepository.findById(1))
                .thenReturn(Optional.of(disbursement));

        when(emiScheduleService.getByDisbursement(disbursement))
                .thenReturn(List.of(emiSchedule));

        mockMvc.perform(
                get("/api/emi/disbursement/1")
        )
        .andExpect(status().isOk());

        verify(disbursementRepository).findById(1);

        verify(emiScheduleService)
                .getByDisbursement(disbursement);
    }

    @Test
    void getOverdue_shouldReturnOverdueEmis() throws Exception {

        when(emiScheduleService.getOverdueEmis())
                .thenReturn(List.of(emiSchedule));

        mockMvc.perform(
                get("/api/emi/overdue")
        )
        .andExpect(status().isOk());

        verify(emiScheduleService)
                .getOverdueEmis();
    }

    @Test
    void getById_shouldReturnEmiSchedule() throws Exception {

        when(emiScheduleService.getById(1))
                .thenReturn(emiSchedule);

        mockMvc.perform(
                get("/api/emi/1")
        )
        .andExpect(status().isOk());

        verify(emiScheduleService)
                .getById(1);
    }
}