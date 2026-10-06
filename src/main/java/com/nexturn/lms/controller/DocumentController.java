package com.nexturn.lms.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.nexturn.lms.dto.DocumentResponse;
import com.nexturn.lms.dto.DocumentVerifyRequest;
import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.DocumentService;
import com.nexturn.lms.service.LoanApplicationService;
import com.nexturn.lms.service.UserService;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private LoanApplicationService applicationService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/application/{applicationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> upload(
            @PathVariable Integer applicationId,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) throws IOException {
        LoanApplication application = applicationService.getById(applicationId);
        Document document = documentService.upload(application, documentType, file);
        return ResponseEntity.ok(new DocumentResponse(document));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<DocumentResponse>> getByApplication(
            @PathVariable Integer applicationId) {
        LoanApplication application = applicationService.getById(applicationId);
        List<DocumentResponse> responses = documentService.getByApplication(application).stream().map(DocumentResponse::new).toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{documentId}/file")
    public ResponseEntity<Resource> viewDocument(
            @PathVariable Integer documentId) throws IOException {
        Document document = documentService.getById(documentId);
        Resource resource = documentService.loadFile(document);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(documentService.getContentType(document)))
                .header(HttpHeaders.CONTENT_DISPOSITION,"inline; filename=\"" + document.getFileName() + "\"")
                .body(resource);
    }

    @PutMapping("/{documentId}/verify")
    public ResponseEntity<DocumentResponse> verify(
            @PathVariable Integer documentId,
            @RequestBody DocumentVerifyRequest request) {
        User officer = null;
        if (request.getOfficerId() != null) {
            officer = userService.getById(request.getOfficerId());
        }
        Document document = documentService.verify(documentId,officer,request.isVerified(),request.getRemarks());
        return ResponseEntity.ok(new DocumentResponse(document));
    }
}