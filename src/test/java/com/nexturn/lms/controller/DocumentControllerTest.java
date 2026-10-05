package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.DocumentRepository;
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

    @Mock
    private DocumentRepository documentRepository;

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
        document.setDocumentId(1);
        document.setFileName("pan.pdf");

        // Required by DocumentResponse
        document.setApplication(application);
    }

    @Test
    void upload_shouldReturnDocument() throws Exception {

        when(applicationService.getById(1))
                .thenReturn(application);

        when(documentService.upload(
                org.mockito.ArgumentMatchers.eq(application),
                org.mockito.ArgumentMatchers.eq("PAN"),
                org.mockito.ArgumentMatchers.eq("pan.pdf"),
                org.mockito.ArgumentMatchers.any(String.class)
        )).thenReturn(document);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "pan.pdf",
                "application/pdf",
                "test pdf content".getBytes()
        );

        mockMvc.perform(
                multipart("/api/documents/application/1")
                        .file(file)
                        .param("documentType", "PAN")
        )
        .andExpect(status().isOk());

        verify(applicationService)
                .getById(1);

        verify(documentService)
                .upload(
                        org.mockito.ArgumentMatchers.eq(application),
                        org.mockito.ArgumentMatchers.eq("PAN"),
                        org.mockito.ArgumentMatchers.eq("pan.pdf"),
                        org.mockito.ArgumentMatchers.any(String.class)
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
    void viewDocument_shouldReturnFile() throws Exception {

        Path tempFile = Files.createTempFile(
                "pan-test-",
                ".pdf"
        );

        try {

            Files.write(
                    tempFile,
                    "test pdf content".getBytes()
            );

            document.setFilePath(tempFile.toString());

            when(documentRepository.findById(1))
                    .thenReturn(Optional.of(document));

            mockMvc.perform(
                    get("/api/documents/1/file")
            )
            .andExpect(status().isOk());

            verify(documentRepository)
                    .findById(1);

        } finally {

            Files.deleteIfExists(tempFile);
        }
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
                        .contentType("application/json")
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
                        .contentType("application/json")
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