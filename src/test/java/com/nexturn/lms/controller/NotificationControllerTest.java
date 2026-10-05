package com.nexturn.lms.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.nexturn.lms.entity.Notification;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.service.NotificationService;
import com.nexturn.lms.service.UserService;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationController notificationController;

    private MockMvc mockMvc;

    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(notificationController)
                .build();

        user = new User();
        user.setUserId(1);

        notification = new Notification();
    }

    @Test
    void getByUser_shouldReturnNotifications() throws Exception {

        when(userService.getById(1))
                .thenReturn(user);

        when(notificationService.getByUser(user))
                .thenReturn(List.of(notification));

        mockMvc.perform(
                get("/api/notifications/user/1")
        )
        .andExpect(status().isOk());

        verify(userService)
                .getById(1);

        verify(notificationService)
                .getByUser(user);
    }

    @Test
    void markAsRead_shouldReturnNotification() throws Exception {

        when(notificationService.markAsRead(1))
                .thenReturn(notification);

        mockMvc.perform(
                put("/api/notifications/1/read")
        )
        .andExpect(status().isOk());

        verify(notificationService)
                .markAsRead(1);
    }
}