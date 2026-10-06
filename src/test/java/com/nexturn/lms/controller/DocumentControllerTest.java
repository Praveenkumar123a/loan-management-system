package com.nexturn.lms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

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
        document.setDocumentId(1);
        document.setFileName("pan.pdf");

        // Required by DocumentResponse
        document.setApplication(application);
    }

    @Test
    void upload_shouldReturnDocument() throws Exception {

        when(applicationService.getById(1)).thenReturn(application);

        when(documentService.upload(eq(application), eq("PAN"), any(MultipartFile.class)))
                .thenReturn(document);

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

        verify(applicationService).getById(1);
        verify(documentService).upload(eq(application), eq("PAN"), any(MultipartFile.class));
    }

    @Test
    void getByApplication_shouldReturnDocuments() throws Exception {

        when(applicationService.getById(1)).thenReturn(application);
        when(documentService.getByApplication(application)).thenReturn(List.of(document));

        mockMvc.perform(get("/api/documents/application/1"))
                .andExpect(status().isOk());

        verify(applicationService).getById(1);
        verify(documentService).getByApplication(application);
    }

    @Test
    void viewDocument_shouldReturnFile() throws Exception {

        Resource resource = new ByteArrayResource("test pdf content".getBytes());

        when(documentService.getById(1)).thenReturn(document);
        when(documentService.loadFile(document)).thenReturn(resource);
        when(documentService.getContentType(document)).thenReturn("application/pdf");

        mockMvc.perform(get("/api/documents/1/file"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/pdf"))
                .andExpect(content().string("test pdf content"));

        verify(documentService).getById(1);
        verify(documentService).loadFile(document);
        verify(documentService).getContentType(document);
    }

    @Test
    void verify_shouldVerifyDocument() throws Exception {

        when(userService.getById(2)).thenReturn(officer);

        when(documentService.verify(1, officer, true, "Document verified"))
                .thenReturn(document);

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

        verify(userService).getById(2);
        verify(documentService).verify(1, officer, true, "Document verified");
    }

    @Test
    void verify_withoutOfficer_shouldAllowNullOfficer() throws Exception {

        when(documentService.verify(1, null, false, "Please resubmit document"))
                .thenReturn(document);

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

        verify(documentService).verify(1, null, false, "Please resubmit document");
    }
}