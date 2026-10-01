package com.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loan.entity.ApplicantProfile;

@Repository 
public interface ApplicantProfileRepository extends JpaRepository<ApplicantProfile,Integer>{

}
