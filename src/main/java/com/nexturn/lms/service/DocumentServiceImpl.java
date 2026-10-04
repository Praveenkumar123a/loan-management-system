package com.nexturn.lms.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nexturn.lms.entity.Document;
import com.nexturn.lms.repository.DocumentRepository;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    DocumentRepository repo;

    @Override
    public String addDocument(Document document) {
        Document doc = repo.save(document);
        String str = "Document inserted " + doc.getDocumentId();
        return str;
    }

    @Override
    public String updateDocument(Document document) {
        repo.save(document);
        String str = "Document updated";
        return str;
    }

    @Override
    public String removeDocument(Integer documentId) {
        repo.deleteById(documentId);
        return "Document deleted";
    }

    @Override
    public List<Document> findAllDocuments() {
        return repo.findAll();
    }

    @Override
    public Document findDocumentById(Integer documentId) {
        Optional<Document> document = repo.findById(documentId);
        if (document.isEmpty()) {
            return null;
        }
        return document.get();
    }
}