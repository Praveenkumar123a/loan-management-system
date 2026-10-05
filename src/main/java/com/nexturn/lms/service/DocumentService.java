package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;

public interface DocumentService {
    Document upload(LoanApplication application, String documentType, String fileName, String filePath);
    List<Document> getByApplication(LoanApplication application);
    Document verify(Integer documentId, User officer, boolean approved, String remarks);
}