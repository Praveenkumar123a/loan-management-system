package com.loan.repository;

<<<<<<< HEAD
public interface NotificationRepository {

=======
import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.entity.Notification;

public interface NotificationRepository extends JpaRepository<Notification,Integer> {
    
>>>>>>> feature/payment
}
