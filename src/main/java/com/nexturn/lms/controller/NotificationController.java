package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexturn.lms.entity.Notification;
import com.nexturn.lms.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestBody Notification notification) {

        Notification savedNotification =
                notificationService.createNotification(notification);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedNotification);
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<Notification> getNotificationById(
            @PathVariable Integer notificationId) {

        return ResponseEntity.ok(
                notificationService.getNotificationById(
                        notificationId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getNotificationsByUser(
            @PathVariable Integer userId) {

        return ResponseEntity.ok(
                notificationService.getNotificationsByUser(userId));
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(
            @PathVariable Integer notificationId) {

        return ResponseEntity.ok(
                notificationService.markAsRead(notificationId));
    }
}