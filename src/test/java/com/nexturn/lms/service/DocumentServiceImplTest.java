package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.InvalidRequestException;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.DocumentRepository;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    private LoanApplication application;
    private User officer;
    private Document document;

    @BeforeEach
    void setUp() {

        application = new LoanApplication();
        application.setApplicationId(1);

        officer = new User();
        officer.setUserId(3);
        officer.setFirstName("Loan");
        officer.setLastName("Officer");
        officer.setEmail("officer@example.com");

        document = new Document();
        document.setDocumentId(1);
        document.setApplication(application);
        document.setDocumentType("AADHAAR");
        document.setFileName("aadhaar.pdf");
        document.setFilePath("/documents/aadhaar.pdf");
        document.setVerificationStatus("PENDING");
    }

    // ---------- upload ----------

    @Test
    void upload_shouldStoreFileAndSaveDocument() throws Exception {

        when(documentRepository.save(any(Document.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MockMultipartFile file = new MockMultipartFile(
                "file", "aadhaar.pdf", "application/pdf", "pdf content".getBytes());

        Document result = documentService.upload(application, "AADHAAR", file);

        Path storedFile = Path.of(result.getFilePath());

        try {
            assertNotNull(result);
            assertEquals(application, result.getApplication());
            assertEquals("AADHAAR", result.getDocumentType());
            assertEquals("aadhaar.pdf", result.getFileName());
            assertEquals("PENDING", result.getVerificationStatus());
            assertTrue(result.getFilePath().endsWith(".pdf"));
            assertTrue(Files.exists(storedFile));

            verify(documentRepository).save(any(Document.class));
        } finally {
            Files.deleteIfExists(storedFile);   // clean up the file the test created
        }
    }

    @Test
    void upload_shouldThrowWhenFileIsEmpty() {

        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.pdf", "application/pdf", new byte[0]);

        assertThrows(InvalidRequestException.class,
                () -> documentService.upload(application, "AADHAAR", emptyFile));

        verify(documentRepository, never()).save(any(Document.class));
    }

    // ---------- read ----------

    @Test
    void getByApplication_shouldReturnDocuments() {

        when(documentRepository.findByApplication(application))
                .thenReturn(List.of(document));

        List<Document> result = documentService.getByApplication(application);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getDocumentId());
        assertEquals("AADHAAR", result.get(0).getDocumentType());

        verify(documentRepository).findByApplication(application);
    }

    @Test
    void getById_shouldReturnDocument() {

        when(documentRepository.findById(1)).thenReturn(Optional.of(document));

        Document result = documentService.getById(1);

        assertEquals(document, result);
    }

    @Test
    void getById_shouldThrowWhenNotFound() {

        when(documentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> documentService.getById(999));
    }

    // ---------- file loading ----------

    @Test
    void loadFile_shouldReturnResourceWhenFileExists() throws Exception {

        Path tempFile = Files.createTempFile("aadhaar-test-", ".pdf");

        try {
            document.setFilePath(tempFile.toString());

            Resource resource = documentService.loadFile(document);

            assertTrue(resource.exists());
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void loadFile_shouldThrowWhenFileMissing() {

        document.setFilePath("/definitely/not/here/missing.pdf");

        assertThrows(ResourceNotFoundException.class,
                () -> documentService.loadFile(document));
    }

    @Test
    void getContentType_shouldFallBackToOctetStream() throws Exception {

        Path tempFile = Files.createTempFile("aadhaar-test-", ".unknownext");

        try {
            document.setFilePath(tempFile.toString());

            assertEquals("application/octet-stream", documentService.getContentType(document));
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    // ---------- verify ----------

    @Test
    void verify_shouldMarkDocumentAsVerified() {

        when(documentRepository.findById(1)).thenReturn(Optional.of(document));
        when(documentRepository.save(document)).thenReturn(document);

        Document result = documentService.verify(1, officer, true, "Document verified successfully");

        assertEquals("VERIFIED", result.getVerificationStatus());
        assertEquals("Document verified successfully", result.getRemarks());
        assertEquals(officer, result.getVerifiedBy());
        assertNotNull(result.getVerifiedAt());

        verify(documentRepository).findById(1);
        verify(documentRepository).save(document);
    }

    @Test
    void verify_shouldRequestResubmissionWhenRejected() {

        when(documentRepository.findById(1)).thenReturn(Optional.of(document));
        when(documentRepository.save(document)).thenReturn(document);

        Document result = documentService.verify(1, officer, false, "Image is not clear");

        assertEquals("RESUBMISSION_REQUIRED", result.getVerificationStatus());
        assertEquals("Image is not clear", result.getRemarks());
        assertEquals(officer, result.getVerifiedBy());
        assertNotNull(result.getVerifiedAt());
    }

    @Test
    void verify_shouldWorkWithoutOfficer() {

        when(documentRepository.findById(1)).thenReturn(Optional.of(document));
        when(documentRepository.save(document)).thenReturn(document);

        Document result = documentService.verify(1, null, true, "Automatically verified");

        assertEquals("VERIFIED", result.getVerificationStatus());
        assertEquals("Automatically verified", result.getRemarks());
        assertNotNull(result.getVerifiedAt());
    }

    @Test
    void verify_shouldThrowExceptionWhenDocumentDoesNotExist() {

        when(documentRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> documentService.verify(999, officer, true, "Verified"));

        verify(documentRepository).findById(999);
        verify(documentRepository, never()).save(any(Document.class));
    }
}