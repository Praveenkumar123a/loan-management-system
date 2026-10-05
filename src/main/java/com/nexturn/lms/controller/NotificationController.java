package com.nexturn.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nexturn.lms.dto.NotificationResponse;
import com.nexturn.lms.entity.Notification;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.NotificationService;
import com.nexturn.lms.service.UserService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getByUser(@PathVariable Integer userId) {
        User user = userService.getById(userId);
        List<NotificationResponse> responses = notificationService.getByUser(user)
                .stream().map(NotificationResponse::new).toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(@PathVariable Integer notificationId) {
        Notification notification = notificationService.markAsRead(notificationId);
        return ResponseEntity.ok(new NotificationResponse(notification));
    }
}