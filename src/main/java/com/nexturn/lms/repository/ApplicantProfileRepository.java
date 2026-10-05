package com.nexturn.lms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.ApplicantProfile;
import com.nexturn.lms.entity.User;

@Repository
public interface ApplicantProfileRepository extends JpaRepository<ApplicantProfile, Integer> {

    Optional<ApplicantProfile> findByUser(User user);
}