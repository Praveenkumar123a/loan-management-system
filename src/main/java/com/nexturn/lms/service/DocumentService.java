package com.nexturn.lms.service;

import java.io.IOException;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.User;

public interface DocumentService {
    Document upload(LoanApplication application, String documentType, MultipartFile file) throws IOException;
    List<Document> getByApplication(LoanApplication application);
    Document getById(Integer documentId);
    Resource loadFile(Document document) throws IOException;
    String getContentType(Document document) throws IOException;
    Document verify(Integer documentId, User officer, boolean approved, String remarks);
}