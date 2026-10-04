package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexturn.lms.entity.Document;
import com.nexturn.lms.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    public ResponseEntity<Document> uploadDocument(
            @RequestBody Document document) {

        Document savedDocument =
                documentService.uploadDocument(document);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedDocument);
    }

    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {

        return ResponseEntity.ok(
                documentService.getAllDocuments());
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<Document> getDocumentById(
            @PathVariable Integer documentId) {

        return ResponseEntity.ok(
                documentService.getDocumentById(documentId));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<Document>> getDocumentsByApplication(
            @PathVariable Integer applicationId) {

        return ResponseEntity.ok(
                documentService.getDocumentsByApplication(applicationId));
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable Integer documentId,
            @RequestBody Document document) {

        return ResponseEntity.ok(
                documentService.updateDocument(
                        documentId,
                        document));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Integer documentId) {

        documentService.deleteDocument(documentId);

        return ResponseEntity.noContent().build();
    }
}