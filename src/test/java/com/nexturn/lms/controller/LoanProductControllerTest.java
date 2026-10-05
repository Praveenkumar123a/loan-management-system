package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.service.LoanProductService;

@ExtendWith(MockitoExtension.class)
class LoanProductControllerTest {

    @Mock
    private LoanProductService productService;

    @InjectMocks
    private LoanProductController productController;

    private MockMvc mockMvc;

    private LoanProduct product;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .build();

        product = new LoanProduct();
    }

    @Test
    void create_shouldReturnProduct() throws Exception {

        when(productService.create(org.mockito.ArgumentMatchers.any(LoanProduct.class)))
                .thenReturn(product);

        String requestBody = """
                {
                    "productName": "Personal Loan",
                    "description": "Personal loan product",
                    "interestRate": 10.5,
                    "minAmount": 50000,
                    "maxAmount": 500000,
                    "minTenureMonths": 12,
                    "maxTenureMonths": 60,
                    "penaltyType": "FIXED",
                    "penaltyValue": 100
                }
                """;

        mockMvc.perform(
                post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(productService)
                .create(org.mockito.ArgumentMatchers.any(LoanProduct.class));
    }

    @Test
    void getAllActive_shouldReturnProducts() throws Exception {

        when(productService.getAllActive())
                .thenReturn(List.of(product));

        mockMvc.perform(
                get("/api/products")
        )
        .andExpect(status().isOk());

        verify(productService)
                .getAllActive();
    }

    @Test
    void getById_shouldReturnProduct() throws Exception {

        when(productService.getById(1))
                .thenReturn(product);

        mockMvc.perform(
                get("/api/products/1")
        )
        .andExpect(status().isOk());

        verify(productService)
                .getById(1);
    }
}