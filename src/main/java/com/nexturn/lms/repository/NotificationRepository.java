package com.nexturn.lms.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.lms.entity.Notification;
import com.nexturn.lms.entity.User;

@Repository 
public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    List<Notification> findByUser(User user);

    List<Notification> findByUserAndTypeAndCreatedAtBetween(
        User user,
        String type,
        LocalDateTime start,
        LocalDateTime end
);
}
