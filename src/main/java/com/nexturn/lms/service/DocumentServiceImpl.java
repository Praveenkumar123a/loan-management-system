package com.nexturn.lms.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.DocumentRepository;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document upload(LoanApplication application, String documentType, String fileName, String filePath) {
        Document document = new Document();
        document.setApplication(application);
        document.setDocumentType(documentType);
        document.setFileName(fileName);
        document.setFilePath(filePath);
        document.setVerificationStatus("PENDING");
        return documentRepository.save(document);
    }

    @Override
    public List<Document> getByApplication(LoanApplication application) {
        return documentRepository.findByApplication(application);
    }

    @Override
    public Document verify(Integer documentId, User officer, boolean approved, String remarks) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + documentId));

        document.setVerificationStatus(
                approved ? "VERIFIED" : "RESUBMISSION_REQUIRED");

        document.setRemarks(remarks);
        document.setVerifiedAt(LocalDateTime.now());

        if (officer != null) {
            document.setVerifiedBy(officer);
        }

        return documentRepository.save(document);
    }
}