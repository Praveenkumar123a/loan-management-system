package com.nexturn.lms.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.entity.Notification;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.EmiScheduleRepository;
import com.nexturn.lms.repository.NotificationRepository;
import com.nexturn.lms.repository.UserRepository;
import com.nexturn.lms.utils.EmiStatus;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final EmiScheduleRepository emiScheduleRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserRepository userRepository,
            EmiScheduleRepository emiScheduleRepository) {

        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.emiScheduleRepository = emiScheduleRepository;
    }

    @Override
    public Notification createNotification(Notification notification) {

        if (notification == null) {
            throw new RuntimeException("Notification cannot be null");
        }

        if (notification.getUser() == null) {
            throw new RuntimeException("User is required");
        }

        if (notification.getMessage() == null ||
                notification.getMessage().isBlank()) {

            throw new RuntimeException("Notification message is required");
        }

        if (notification.getType() == null ||
                notification.getType().isBlank()) {

            throw new RuntimeException("Notification type is required");
        }

        return notificationRepository.save(notification);
    }

    @Override
    public Notification getNotificationById(Integer notificationId) {

        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
    }

    @Override
    public List<Notification> getNotificationsByUser(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return notificationRepository.findByUser(user);
    }

    @Override
    public Notification markAsRead(Integer notificationId) {

        Notification notification = getNotificationById(notificationId);

        notification.setRead(true);

        return notificationRepository.save(notification);
    }

    @Scheduled(cron = "0 0 9 * * *")
    @Override
    public void sendUpcomingEmiNotifications() {

        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<EmiSchedule> upcomingEmis = emiScheduleRepository.findByDueDateAndStatus(
                tomorrow,
                EmiStatus.PENDING);

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = tomorrow.atStartOfDay();

        for (EmiSchedule emi : upcomingEmis) {

            User applicant = emi.getDisbursement()
                    .getApplication()
                    .getApplicant();

            List<Notification> existingNotifications = notificationRepository
                    .findByUserAndTypeAndCreatedAtBetween(
                            applicant,
                            "EMI_DUE",
                            startOfDay,
                            endOfDay);

            if (!existingNotifications.isEmpty()) {
                continue;
            }

            Notification notification = new Notification();

            notification.setUser(applicant);
            notification.setType("EMI_DUE");

            notification.setMessage(
                    "Your EMI #" + emi.getInstallmentNo()
                            + " of ₹" + emi.getEmiAmount()
                            + " is due tomorrow.");

            createNotification(notification);
        }
    }

    @Scheduled(cron = "0 0 10 * * *")
    @Override
    public void sendOverdueEmiNotifications() {

        LocalDate today = LocalDate.now();

        List<EmiSchedule> overdueEmis = emiScheduleRepository.findByDueDateBeforeAndStatus(
                today,
                EmiStatus.PENDING);

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        for (EmiSchedule emi : overdueEmis) {

            User applicant = emi.getDisbursement()
                    .getApplication()
                    .getApplicant();

            List<Notification> existingNotifications = notificationRepository
                    .findByUserAndTypeAndCreatedAtBetween(
                            applicant,
                            "EMI_OVERDUE",
                            startOfDay,
                            endOfDay);

            if (!existingNotifications.isEmpty()) {
                continue;
            }

            Notification notification = new Notification();

            notification.setUser(applicant);
            notification.setType("EMI_OVERDUE");

            notification.setMessage(
                    "Your EMI #" + emi.getInstallmentNo()
                            + " is overdue. Please make the payment as soon as possible.");

            createNotification(notification);
        }
    }


}

