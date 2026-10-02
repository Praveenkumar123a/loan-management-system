package com.nexturn.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.Document;

@Repository 
public interface DocumentRepository extends JpaRepository<Document,Integer> {

}
