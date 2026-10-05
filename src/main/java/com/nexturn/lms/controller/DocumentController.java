package com.nexturn.lms.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nexturn.lms.dto.DocumentUploadRequest;
import com.nexturn.lms.dto.DocumentVerifyRequest;
import com.nexturn.lms.dto.DocumentResponse;
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
 @Autowired private DocumentService documentService;
 @Autowired private LoanApplicationService applicationService;
 @Autowired private UserService userService;

 @PostMapping("/application/{applicationId}")
 public ResponseEntity<DocumentResponse> upload(@PathVariable Integer applicationId,@RequestBody DocumentUploadRequest request) {
  LoanApplication application=applicationService.getById(applicationId);
  Document document=documentService.upload(application,request.getDocumentType(),request.getFileName(),request.getFilePath());
  return ResponseEntity.ok(new DocumentResponse(document));
 }

 @GetMapping("/application/{applicationId}")
 public ResponseEntity<List<DocumentResponse>> getByApplication(@PathVariable Integer applicationId) {
  LoanApplication application=applicationService.getById(applicationId);
  List<DocumentResponse> responses=documentService.getByApplication(application).stream().map(DocumentResponse::new).toList();
  return ResponseEntity.ok(responses);
 }

 @PutMapping("/{documentId}/verify")
 public ResponseEntity<DocumentResponse> verify(@PathVariable Integer documentId,@RequestBody DocumentVerifyRequest request) {
  User officer=null;
  if(request.getOfficerId()!=null) officer=userService.getById(request.getOfficerId());
  Document document=documentService.verify(documentId,officer,request.isVerified(),request.getRemarks());
  return ResponseEntity.ok(new DocumentResponse(document));
 }
}