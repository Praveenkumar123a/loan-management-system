package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
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

    @Test
    void upload_shouldSaveDocument() {

        when(documentRepository.save(any(Document.class)))
                .thenReturn(document);

        Document result = documentService.upload(
                application,
                "AADHAAR",
                "aadhaar.pdf",
                "/documents/aadhaar.pdf"
        );

        assertNotNull(result);
        assertEquals(1, result.getDocumentId());
        assertEquals(application, result.getApplication());
        assertEquals("AADHAAR", result.getDocumentType());
        assertEquals("aadhaar.pdf", result.getFileName());
        assertEquals(
                "/documents/aadhaar.pdf",
                result.getFilePath()
        );
        assertEquals(
                "PENDING",
                result.getVerificationStatus()
        );

        verify(documentRepository)
                .save(any(Document.class));
    }

    @Test
    void getByApplication_shouldReturnDocuments() {

        when(documentRepository.findByApplication(application))
                .thenReturn(List.of(document));

        List<Document> result =
                documentService.getByApplication(application);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(
                1,
                result.get(0).getDocumentId()
        );
        assertEquals(
                "AADHAAR",
                result.get(0).getDocumentType()
        );

        verify(documentRepository)
                .findByApplication(application);
    }

    @Test
    void verify_shouldMarkDocumentAsVerified() {

        when(documentRepository.findById(1))
                .thenReturn(Optional.of(document));

        when(documentRepository.save(document))
                .thenReturn(document);

        Document result = documentService.verify(
                1,
                officer,
                true,
                "Document verified successfully"
        );

        assertEquals(
                "VERIFIED",
                result.getVerificationStatus()
        );

        assertEquals(
                "Document verified successfully",
                result.getRemarks()
        );

        assertEquals(
                officer,
                result.getVerifiedBy()
        );

        assertNotNull(result.getVerifiedAt());

        verify(documentRepository)
                .findById(1);

        verify(documentRepository)
                .save(document);
    }

    @Test
    void verify_shouldRequestResubmissionWhenRejected() {

        when(documentRepository.findById(1))
                .thenReturn(Optional.of(document));

        when(documentRepository.save(document))
                .thenReturn(document);

        Document result = documentService.verify(
                1,
                officer,
                false,
                "Image is not clear"
        );

        assertEquals(
                "RESUBMISSION_REQUIRED",
                result.getVerificationStatus()
        );

        assertEquals(
                "Image is not clear",
                result.getRemarks()
        );

        assertEquals(
                officer,
                result.getVerifiedBy()
        );

        assertNotNull(result.getVerifiedAt());

        verify(documentRepository)
                .findById(1);

        verify(documentRepository)
                .save(document);
    }

    @Test
    void verify_shouldWorkWithoutOfficer() {

        when(documentRepository.findById(1))
                .thenReturn(Optional.of(document));

        when(documentRepository.save(document))
                .thenReturn(document);

        Document result = documentService.verify(
                1,
                null,
                true,
                "Automatically verified"
        );

        assertEquals(
                "VERIFIED",
                result.getVerificationStatus()
        );

        assertEquals(
                "Automatically verified",
                result.getRemarks()
        );

        assertNotNull(result.getVerifiedAt());

        verify(documentRepository)
                .findById(1);

        verify(documentRepository)
                .save(document);
    }

    @Test
    void verify_shouldThrowExceptionWhenDocumentDoesNotExist() {

        when(documentRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> documentService.verify(
                        999,
                        officer,
                        true,
                        "Verified"
                )
        );

        verify(documentRepository)
                .findById(999);
    }
}