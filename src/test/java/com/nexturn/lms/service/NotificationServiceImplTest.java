package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.Notification;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setUserId(1);
    }

    @Test
    void notify_shouldCreateNotification() {

        when(notificationRepository.save(any(Notification.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Notification result =
                notificationService.notify(
                        user,
                        "Loan application approved",
                        "LOAN"
                );

        assertNotNull(result);

        assertEquals(
                user,
                result.getUser()
        );

        assertEquals(
                "Loan application approved",
                result.getMessage()
        );

        assertEquals(
                "LOAN",
                result.getType()
        );

        assertFalse(
                result.isRead()
        );

        assertNotNull(
                result.getCreatedAt()
        );

        verify(notificationRepository)
                .save(any(Notification.class));
    }

    @Test
    void getByUser_shouldReturnNotifications() {

        Notification notification1 =
                new Notification();

        notification1.setUser(user);
        notification1.setMessage(
                "Loan approved"
        );
        notification1.setType("LOAN");
        notification1.setRead(false);
        notification1.setCreatedAt(
                LocalDateTime.now()
        );

        Notification notification2 =
                new Notification();

        notification2.setUser(user);
        notification2.setMessage(
                "EMI payment received"
        );
        notification2.setType("PAYMENT");
        notification2.setRead(false);
        notification2.setCreatedAt(
                LocalDateTime.now()
        );

        List<Notification> notifications =
                List.of(
                        notification1,
                        notification2
                );

        when(notificationRepository.findByUser(user))
                .thenReturn(notifications);

        List<Notification> result =
                notificationService.getByUser(user);

        assertNotNull(result);

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                notification1,
                result.get(0)
        );

        assertEquals(
                notification2,
                result.get(1)
        );

        verify(notificationRepository)
                .findByUser(user);
    }

    @Test
    void getByUser_shouldReturnEmptyListWhenNoNotificationsExist() {

        when(notificationRepository.findByUser(user))
                .thenReturn(List.of());

        List<Notification> result =
                notificationService.getByUser(user);

        assertNotNull(result);

        assertEquals(
                0,
                result.size()
        );

        verify(notificationRepository)
                .findByUser(user);
    }

    @Test
    void markAsRead_shouldMarkNotificationAsRead() {

        Notification notification =
                new Notification();

        notification.setNotificationId(1);
        notification.setUser(user);
        notification.setMessage(
                "Your loan has been approved"
        );
        notification.setType("LOAN");
        notification.setRead(false);

        when(notificationRepository.findById(1))
                .thenReturn(Optional.of(notification));

        when(notificationRepository.save(notification))
                .thenReturn(notification);

        Notification result =
                notificationService.markAsRead(1);

        assertNotNull(result);

        assertTrue(
                result.isRead()
        );

        assertEquals(
                1,
                result.getNotificationId()
        );

        assertEquals(
                "Your loan has been approved",
                result.getMessage()
        );

        verify(notificationRepository)
                .findById(1);

        verify(notificationRepository)
                .save(notification);
    }

    @Test
    void markAsRead_shouldThrowExceptionWhenNotificationDoesNotExist() {

        when(notificationRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> notificationService.markAsRead(999)
        );

        verify(notificationRepository)
                .findById(999);
    }
}