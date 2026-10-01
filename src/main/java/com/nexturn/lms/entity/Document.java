package com.nexturn.lms.entity;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Integer documentId;
    @ManyToOne
    @JoinColumn(name = "application_id", nullable = false)
    private LoanApplication application;
    @Column(name = "document_type", length = 40, nullable = false)
    private String documentType;
    @Column(name = "file_name", length = 150, nullable = false)
    private String fileName;
    @Column(name = "file_path", length = 255, nullable = false)
    private String filePath;
    @Column(name = "verification_status", length = 15)
    private String verificationStatus = "PENDING";
    @Column(name = "remarks", length = 255)
    private String remarks;
    @ManyToOne
    @JoinColumn(name = "verified_by")
    private User verifiedBy;
    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;
    @CreationTimestamp
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;
    public Document() {
    }
    public Integer getDocumentId() {
        return documentId;
    }
    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }
    public LoanApplication getApplication() {
        return application;
    }
    public void setApplication(LoanApplication application) {
        this.application = application;
    }
    public String getDocumentType() {
        return documentType;
    }
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getVerificationStatus() {
        return verificationStatus;
    }
    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public User getVerifiedBy() {
        return verifiedBy;
    }
    public void setVerifiedBy(User verifiedBy) {
        this.verifiedBy = verifiedBy;
    }
    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }
    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }
    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }
    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}