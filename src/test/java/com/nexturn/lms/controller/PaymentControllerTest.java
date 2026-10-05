package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.entity.Payment;
import com.nexturn.lms.service.PaymentService;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private MockMvc mockMvc;

    private Payment payment;
    private EmiSchedule emiSchedule;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(paymentController)
                .build();

        emiSchedule = new EmiSchedule();
        emiSchedule.setEmiId(1);

        payment = new Payment();
        payment.setPaymentId(1);
        payment.setEmiSchedule(emiSchedule);
    }

    @Test
    void payEmi_shouldReturnPayment() throws Exception {

        when(paymentService.payEmi(
                1,
                "UPI",
                "TXN123"
        )).thenReturn(payment);

        String requestBody = """
                {
                    "paymentMode": "UPI",
                    "transactionRef": "TXN123"
                }
                """;

        mockMvc.perform(
                post("/api/payments/emi/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(paymentService).payEmi(
                1,
                "UPI",
                "TXN123"
        );
    }
}