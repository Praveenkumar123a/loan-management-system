package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.DocumentService;
import com.nexturn.lms.service.LoanApplicationService;
import com.nexturn.lms.service.UserService;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @Mock
    private LoanApplicationService applicationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private DocumentController documentController;

    private MockMvc mockMvc;

    private LoanApplication application;
    private User officer;
    private Document document;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(documentController)
                .build();

        application = new LoanApplication();
        application.setApplicationId(1);

        officer = new User();
        officer.setUserId(2);

        document = new Document();
    }

    @Test
    void upload_shouldReturnDocument() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        when(documentService.upload(
                application,
                "PAN",
                "pan.pdf",
                "/uploads/pan.pdf"
        )).thenReturn(document);

        String requestBody = """
                {
                    "documentType": "PAN",
                    "fileName": "pan.pdf",
                    "filePath": "/uploads/pan.pdf"
                }
                """;

        mockMvc.perform(
                post("/api/documents/application/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getById(1);

        verify(documentService)
                .upload(
                        application,
                        "PAN",
                        "pan.pdf",
                        "/uploads/pan.pdf"
                );
    }

    @Test
    void getByApplication_shouldReturnDocuments() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        when(documentService.getByApplication(application))
                .thenReturn(List.of(document));

        mockMvc.perform(
                get("/api/documents/application/1")
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getById(1);

        verify(documentService)
                .getByApplication(application);
    }

    @Test
    void verify_shouldVerifyDocument() throws Exception {

        when(userService.getById(2))
                .thenReturn(officer);

        when(documentService.verify(
                1,
                officer,
                true,
                "Document verified"
        )).thenReturn(document);

        String requestBody = """
                {
                    "officerId": 2,
                    "verified": true,
                    "remarks": "Document verified"
                }
                """;

        mockMvc.perform(
                put("/api/documents/1/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(userService)
                .getById(2);

        verify(documentService)
                .verify(
                        1,
                        officer,
                        true,
                        "Document verified"
                );
    }

    @Test
    void verify_withoutOfficer_shouldAllowNullOfficer() throws Exception {

        when(documentService.verify(
                1,
                null,
                false,
                "Please resubmit document"
        )).thenReturn(document);

        String requestBody = """
                {
                    "officerId": null,
                    "verified": false,
                    "remarks": "Please resubmit document"
                }
                """;

        mockMvc.perform(
                put("/api/documents/1/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
        .andExpect(status().isOk());

        verify(documentService)
                .verify(
                        1,
                        null,
                        false,
                        "Please resubmit document"
                );
    }
}