package com.loan.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.Notification;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.EmiScheduleRepository;
import com.nexturn.lms.repository.NotificationRepository;
import com.nexturn.lms.repository.UserRepository;
import com.nexturn.lms.service.NotificationServiceImpl;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmiScheduleRepository emiScheduleRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

@Test
void testUpcomingEmiNotification() {

    User user = new User();

    LoanApplication application = new LoanApplication();
    application.setApplicant(user);

    Disbursement disbursement = new Disbursement();
    disbursement.setApplication(application);

    EmiSchedule emi = new EmiSchedule();
    emi.setInstallmentNo(1);
    emi.setEmiAmount(new java.math.BigDecimal("5000"));
    emi.setDisbursement(disbursement);

    when(emiScheduleRepository.findByDueDateAndStatus(any(), any()))
            .thenReturn(java.util.List.of(emi));

    when(notificationRepository
            .findByUserAndTypeAndCreatedAtBetween(
                    any(),
                    eq("EMI_DUE"),
                    any(),
                    any()))
            .thenReturn(java.util.Collections.emptyList());

    notificationService.sendUpcomingEmiNotifications();

    verify(notificationRepository, times(1))
        .save(argThat(notification ->
                notification.getUser() == user
                && notification.getType().equals("EMI_DUE")
                && notification.getMessage().contains("EMI #1")
                && notification.getMessage().contains("₹5000")
        ));
}

@Test
void testUpcomingEmiNotificationNotDuplicated() {

    User user = new User();

    LoanApplication application = new LoanApplication();
    application.setApplicant(user);

    Disbursement disbursement = new Disbursement();
    disbursement.setApplication(application);

    EmiSchedule emi = new EmiSchedule();
    emi.setInstallmentNo(1);
    emi.setEmiAmount(new java.math.BigDecimal("5000"));
    emi.setDisbursement(disbursement);

    when(emiScheduleRepository.findByDueDateAndStatus(any(), any()))
            .thenReturn(java.util.List.of(emi));

    // Simulate that notification already exists
    when(notificationRepository
            .findByUserAndTypeAndCreatedAtBetween(
                    any(),
                    eq("EMI_DUE"),
                    any(),
                    any()))
            .thenReturn(java.util.List.of(new Notification()));

    notificationService.sendUpcomingEmiNotifications();

    // Notification should NOT be saved again
    verify(notificationRepository, never())
            .save(any(Notification.class));
}

@Test
void testOverdueEmiNotification() {

    User user = new User();

    LoanApplication application = new LoanApplication();
    application.setApplicant(user);

    Disbursement disbursement = new Disbursement();
    disbursement.setApplication(application);

    EmiSchedule emi = new EmiSchedule();
    emi.setInstallmentNo(2);
    emi.setEmiAmount(new java.math.BigDecimal("7000"));
    emi.setDisbursement(disbursement);

    when(emiScheduleRepository
            .findByDueDateBeforeAndStatus(any(), any()))
            .thenReturn(java.util.List.of(emi));

    when(notificationRepository
            .findByUserAndTypeAndCreatedAtBetween(
                    any(),
                    eq("EMI_OVERDUE"),
                    any(),
                    any()))
            .thenReturn(java.util.Collections.emptyList());

    notificationService.sendOverdueEmiNotifications();

    verify(notificationRepository, times(1))
            .save(argThat(notification ->
                    notification.getUser() == user
                    && notification.getType().equals("EMI_OVERDUE")
                    && notification.getMessage().contains("EMI #2")
                    && notification.getMessage().contains("overdue")
            ));
}
@Test
void testOverdueEmiNotificationNotDuplicated() {

    User user = new User();

    LoanApplication application = new LoanApplication();
    application.setApplicant(user);

    Disbursement disbursement = new Disbursement();
    disbursement.setApplication(application);

    EmiSchedule emi = new EmiSchedule();
    emi.setInstallmentNo(2);
    emi.setEmiAmount(new java.math.BigDecimal("7000"));
    emi.setDisbursement(disbursement);

    when(emiScheduleRepository
            .findByDueDateBeforeAndStatus(any(), any()))
            .thenReturn(java.util.List.of(emi));

    // Simulate existing overdue notification
    when(notificationRepository
            .findByUserAndTypeAndCreatedAtBetween(
                    any(),
                    eq("EMI_OVERDUE"),
                    any(),
                    any()))
            .thenReturn(java.util.List.of(new Notification()));

    notificationService.sendOverdueEmiNotifications();

    // It must not create another notification
    verify(notificationRepository, never())
            .save(any(Notification.class));
}
}