package com.nexturn.lms.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.InvalidRequestException;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.DocumentRepository;

@Service
public class DocumentServiceImpl implements DocumentService {
    private static final Path UPLOAD_DIR = Paths.get("uploads", "documents");

    @Autowired
    private DocumentRepository documentRepository;

    @Override
    public Document upload(LoanApplication application, String documentType, MultipartFile file)
            throws IOException {

        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException("Please select a file.");
        }
        Files.createDirectories(UPLOAD_DIR);
        String originalName = file.getOriginalFilename();
        String extension = "";

        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf("."));
        }

        String storedName = UUID.randomUUID() + extension;
        Path targetPath = UPLOAD_DIR.resolve(storedName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        Document document = new Document();
        document.setApplication(application);
        document.setDocumentType(documentType);
        document.setFileName(originalName);
        document.setFilePath(targetPath.toString());
        document.setVerificationStatus("PENDING");
        return documentRepository.save(document);
    }

    @Override
    public List<Document> getByApplication(LoanApplication application) {
        return documentRepository.findByApplication(application);
    }

    @Override
    public Document getById(Integer documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found: " + documentId));
    }

    @Override
    public Resource loadFile(Document document) throws IOException {
        Path filePath = Paths.get(document.getFilePath()).toAbsolutePath().normalize();
        if (!Files.exists(filePath)) {
            throw new ResourceNotFoundException("Document file not found.");
        }
        return new UrlResource(filePath.toUri());
    }

    @Override
    public String getContentType(Document document) throws IOException {
        Path filePath = Paths.get(document.getFilePath()).toAbsolutePath().normalize();
        String contentType = Files.probeContentType(filePath);
        return contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }

    @Override
    public Document verify(Integer documentId, User officer, boolean approved, String remarks) {
        Document document = getById(documentId);

        document.setVerificationStatus(approved ? "VERIFIED" : "RESUBMISSION_REQUIRED");
        document.setRemarks(remarks);
        document.setVerifiedAt(LocalDateTime.now());
        if (officer != null) {
            document.setVerifiedBy(officer);
        }
        return documentRepository.save(document);
    }
}