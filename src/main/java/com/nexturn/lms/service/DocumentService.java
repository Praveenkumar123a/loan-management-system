package com.nexturn.lms.service;

import java.util.List;
import com.nexturn.lms.entity.Document;

public interface DocumentService {
    String addDocument(Document document);
    String updateDocument(Document document);
    String removeDocument(Integer documentId);
    List<Document> findAllDocuments();
    Document findDocumentById(Integer documentId);
}