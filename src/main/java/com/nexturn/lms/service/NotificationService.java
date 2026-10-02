package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.Notification;

public interface NotificationService {

    Notification createNotification(Notification notification);

    Notification getNotificationById(Integer notificationId);

    List<Notification> getNotificationsByUser(Integer userId);

    Notification markAsRead(Integer notificationId);

    void sendUpcomingEmiNotifications();

    void sendOverdueEmiNotifications();
}
