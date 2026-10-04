package com.nexturn.lms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.Document;
import com.nexturn.lms.entity.LoanApplication;

@Repository
public interface DocumentRepository
        extends JpaRepository<Document, Integer> {

    List<Document> findByApplication(LoanApplication application);
}