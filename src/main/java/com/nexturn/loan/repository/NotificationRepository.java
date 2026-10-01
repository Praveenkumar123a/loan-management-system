package com.nexturn.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.loan.entity.Notification;

@Repository 
public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    
}
