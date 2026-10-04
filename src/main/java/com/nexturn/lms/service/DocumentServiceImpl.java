package com.nexturn.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.exception.DocumentNotFoundException;
import com.nexturn.lms.exception.LoanApplicationNotFoundException;
import com.nexturn.lms.repository.DocumentRepository;
import com.nexturn.lms.repository.LoanApplicationRepository;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    public DocumentServiceImpl(
            DocumentRepository documentRepository,
            LoanApplicationRepository loanApplicationRepository) {

        this.documentRepository = documentRepository;
        this.loanApplicationRepository = loanApplicationRepository;
    }

    @Override
    public Document uploadDocument(Document document) {

        if (document == null) {
            throw new IllegalArgumentException(
                    "Document cannot be null");
        }

        return documentRepository.save(document);
    }

    @Override
    public List<Document> getAllDocuments() {

        return documentRepository.findAll();
    }

    @Override
    public Document getDocumentById(Integer documentId) {

        return documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new DocumentNotFoundException(
                                "Document not found"));
    }

    @Override
    public List<Document> getDocumentsByApplication(
            Integer applicationId) {

        LoanApplication application =
                loanApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new LoanApplicationNotFoundException(
                                        "Loan application not found"));

        return documentRepository.findByApplication(application);
    }

    @Override
    public Document updateDocument(
            Integer documentId,
            Document document) {

        Document existingDocument =
                documentRepository.findById(documentId)
                        .orElseThrow(() ->
                                new DocumentNotFoundException(
                                        "Document not found"));

        existingDocument.setApplication(
                document.getApplication());

        existingDocument.setDocumentType(
                document.getDocumentType());

        existingDocument.setFileName(
                document.getFileName());

        existingDocument.setFilePath(
                document.getFilePath());

        existingDocument.setVerificationStatus(
                document.getVerificationStatus());

        existingDocument.setRemarks(
                document.getRemarks());

        existingDocument.setVerifiedBy(
                document.getVerifiedBy());

        existingDocument.setVerifiedAt(
                document.getVerifiedAt());

        return documentRepository.save(existingDocument);
    }

    @Override
    public void deleteDocument(Integer documentId) {

        Document existingDocument =
                documentRepository.findById(documentId)
                        .orElseThrow(() ->
                                new DocumentNotFoundException(
                                        "Document not found"));

        documentRepository.delete(existingDocument);
    }
}