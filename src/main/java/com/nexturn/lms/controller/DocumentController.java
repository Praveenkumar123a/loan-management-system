package com.nexturn.lms.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import com.nexturn.lms.repository.DocumentRepository;
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

    @Autowired
    private DocumentRepository documentRepository;

    @PostMapping(value = "/application/{applicationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> upload(
            @PathVariable Integer applicationId,
            @RequestParam("documentType") String documentType,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("Please select a file.");
        }

        LoanApplication application = applicationService.getById(applicationId);

        Path uploadDir = Paths.get("uploads", "documents");
        Files.createDirectories(uploadDir);

        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String storedName = UUID.randomUUID() + extension;
        Path targetPath = uploadDir.resolve(storedName);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        Document document = documentService.upload(
                application,
                documentType,
                originalName,
                targetPath.toString()
        );

        return ResponseEntity.ok(new DocumentResponse(document));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<DocumentResponse>> getByApplication(
            @PathVariable Integer applicationId) {

        LoanApplication application = applicationService.getById(applicationId);

        List<DocumentResponse> responses = documentService
                .getByApplication(application)
                .stream()
                .map(DocumentResponse::new)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{documentId}/file")
    public ResponseEntity<Resource> viewDocument(
            @PathVariable Integer documentId) throws IOException {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found: " + documentId));

        Path filePath = Paths.get(document.getFilePath()).toAbsolutePath().normalize();

        if (!Files.exists(filePath)) {
            throw new RuntimeException("Document file not found.");
        }

        Resource resource = new UrlResource(filePath.toUri());

        String contentType = Files.probeContentType(filePath);

        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + document.getFileName() + "\""
                )
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

        Document document = documentService.verify(
                documentId,
                officer,
                request.isVerified(),
                request.getRemarks()
        );

        return ResponseEntity.ok(new DocumentResponse(document));
    }
}