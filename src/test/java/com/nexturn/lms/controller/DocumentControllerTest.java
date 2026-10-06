package com.nexturn.lms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nexturn.lms.entity.Document;
import com.nexturn.lms.service.DocumentService;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {

    @Mock
    private DocumentService documentService;

    @InjectMocks
    private DocumentController documentController;

    private MockMvc mockMvc;

    private Document document;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(documentController)
                .build();

        objectMapper = new ObjectMapper();

        document = new Document();
        document.setDocumentId(1);
        document.setFileName("pan.pdf");
    }

    @Test
    void uploadDocument_shouldReturnCreated() throws Exception {

        when(documentService.uploadDocument(any(Document.class)))
                .thenReturn(document);

        mockMvc.perform(
                post("/api/documents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(document))
        )
        .andExpect(status().isCreated());

        verify(documentService)
                .uploadDocument(any(Document.class));
    }

    @Test
    void getAllDocuments_shouldReturnDocuments() throws Exception {

        when(documentService.getAllDocuments())
                .thenReturn(List.of(document));

        mockMvc.perform(
                get("/api/documents")
        )
        .andExpect(status().isOk());

        verify(documentService)
                .getAllDocuments();
    }

    @Test
    void getDocumentById_shouldReturnDocument() throws Exception {

        when(documentService.getDocumentById(1))
                .thenReturn(document);

        mockMvc.perform(
                get("/api/documents/1")
        )
        .andExpect(status().isOk());

        verify(documentService)
                .getDocumentById(1);
    }

    @Test
    void getDocumentsByApplication_shouldReturnDocuments() throws Exception {

        when(documentService.getDocumentsByApplication(1))
                .thenReturn(List.of(document));

        mockMvc.perform(
                get("/api/documents/application/1")
        )
        .andExpect(status().isOk());

        verify(documentService)
                .getDocumentsByApplication(1);
    }

    @Test
    void updateDocument_shouldReturnDocument() throws Exception {

        when(documentService.updateDocument(eq(1), any(Document.class)))
                .thenReturn(document);

        mockMvc.perform(
                put("/api/documents/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(document))
        )
        .andExpect(status().isOk());

        verify(documentService)
                .updateDocument(eq(1), any(Document.class));
    }

    @Test
    void deleteDocument_shouldReturnNoContent() throws Exception {

        mockMvc.perform(
                delete("/api/documents/1")
        )
        .andExpect(status().isNoContent());

        verify(documentService)
                .deleteDocument(1);
    }
}