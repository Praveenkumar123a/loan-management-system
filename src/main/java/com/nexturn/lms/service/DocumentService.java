package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.Document;

public interface DocumentService {

    Document uploadDocument(Document document);

    List<Document> getAllDocuments();

    Document getDocumentById(Integer documentId);

    List<Document> getDocumentsByApplication(Integer applicationId);

    Document updateDocument(
            Integer documentId,
            Document document);

    void deleteDocument(Integer documentId);
}