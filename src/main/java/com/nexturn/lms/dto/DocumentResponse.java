package com.nexturn.lms.dto;

import java.time.LocalDateTime;

import com.nexturn.lms.entity.Document;

public class DocumentResponse {
    private Integer documentId;
    private Integer applicationId;
    private String documentType;
    private String fileName;
    private String verificationStatus;
    private String remarks;
    private LocalDateTime uploadedAt;

    public DocumentResponse(Document doc) {
        this.documentId = doc.getDocumentId();
        this.applicationId = doc.getApplication().getApplicationId();
        this.documentType = doc.getDocumentType();
        this.fileName = doc.getFileName();
        this.verificationStatus = doc.getVerificationStatus();
        this.remarks = doc.getRemarks();
        this.uploadedAt = doc.getUploadedAt();
    }

    public Integer getDocumentId() { return documentId; }
    public Integer getApplicationId() { return applicationId; }
    public String getDocumentType() { return documentType; }
    public String getFileName() { return fileName; }
    public String getVerificationStatus() { return verificationStatus; }
    public String getRemarks() { return remarks; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
}