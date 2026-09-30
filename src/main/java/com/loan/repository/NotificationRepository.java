package com.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    
}
