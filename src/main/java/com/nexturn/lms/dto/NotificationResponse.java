package com.nexturn.lms.dto;

import java.time.LocalDateTime;

import com.nexturn.lms.entity.Notification;

public class NotificationResponse {
    private Integer notificationId;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationResponse(Notification n) {
        this.notificationId = n.getNotificationId();
        this.message = n.getMessage();
        this.type = n.getType();
        this.isRead = n.isRead();
        this.createdAt = n.getCreatedAt();
    }

    public Integer getNotificationId() { return notificationId; }
    public String getMessage() { return message; }
    public String getType() { return type; }
    public boolean isRead() { return isRead; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}