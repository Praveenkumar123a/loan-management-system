package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.Notification;
import com.nexturn.lms.entity.User;

public interface NotificationService {
    Notification notify(User user, String message, String type);
    List<Notification> getByUser(User user);
    Notification markAsRead(Integer notificationId);
}