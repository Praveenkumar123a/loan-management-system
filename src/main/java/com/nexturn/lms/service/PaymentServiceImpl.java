package com.nexturn.lms.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nexturn.lms.entity.AuditLog;
import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.entity.Notification;
import com.nexturn.lms.entity.Payment;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.EmiScheduleRepository;
import com.nexturn.lms.repository.PaymentRepository;
import com.nexturn.lms.utils.EmiStatus;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final EmiScheduleRepository emiScheduleRepository;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            EmiScheduleRepository emiScheduleRepository,
            AuditLogService auditLogService,
            NotificationService notificationService) {

        this.paymentRepository = paymentRepository;
        this.emiScheduleRepository = emiScheduleRepository;
        this.auditLogService = auditLogService;
        this.notificationService = notificationService;
    }

    @Override
    public Payment getPaymentById(Integer paymentId) {

        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    @Override
    public List<Payment> getPaymentsByEmi(Integer emiId) {

        return paymentRepository.findByEmiSchedule_EmiId(emiId);
    }

    @Override
    @Transactional
    public Payment makePayment(Payment payment) {

        // 1. Validate payment
        if (payment == null) {
            throw new RuntimeException("Payment cannot be null");
        }

        // 2. Validate EMI schedule
        if (payment.getEmiSchedule() == null) {
            throw new RuntimeException("EMI schedule is required");
        }

        Integer emiId = payment.getEmiSchedule().getEmiId();

        // 3. Find EMI
        EmiSchedule emi = emiScheduleRepository.findById(emiId)
                .orElseThrow(() -> new RuntimeException("EMI not found"));

        // 4. Validate payment amount
        if (payment.getAmountPaid() == null
                || payment.getAmountPaid().signum() <= 0) {

            throw new RuntimeException(
                    "Payment amount must be greater than zero");
        }

        // 5. Generate transaction reference
        String transactionRef = "TXN-" + UUID.randomUUID();

        payment.setTransactionRef(transactionRef);

        // 6. Set payment date if not provided
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }

        /*
         * 7. Calculate late payment penalty
         */

        BigDecimal penalty = BigDecimal.ZERO;

        long lateDays = ChronoUnit.DAYS.between(
                emi.getDueDate(),
                payment.getPaymentDate().toLocalDate());

        if (lateDays > 0) {

            // Temporary penalty policy:
            // ₹100 per late day
            BigDecimal dailyPenalty = new BigDecimal("100");

            penalty = dailyPenalty.multiply(
                    BigDecimal.valueOf(lateDays));
        }

        payment.setPenaltyPaid(penalty);
        emi.setPenaltyAmount(penalty);

        /*
         * 8. Calculate previous amount paid
         */

        BigDecimal previousPaid =
                paymentRepository.getTotalAmountPaidForEmi(emiId);

        if (previousPaid == null) {
            previousPaid = BigDecimal.ZERO;
        }

        /*
         * 9. Calculate total amount paid
         *    including current payment
         */

        BigDecimal totalPaid =
                previousPaid.add(payment.getAmountPaid());

        /*
         * 10. Calculate total amount due
         */

        BigDecimal totalDue =
                emi.getEmiAmount().add(penalty);

        /*
         * 11. Check whether EMI is completely paid
         */

        if (totalPaid.compareTo(totalDue) >= 0) {

            emi.setStatus(EmiStatus.PAID);

            emi.setPaidDate(
                    payment.getPaymentDate().toLocalDate());
        }

        /*
         * 12. Attach EMI to payment
         */

        payment.setEmiSchedule(emi);

        /*
         * 13. Save payment
         */

        Payment savedPayment =
                paymentRepository.save(payment);

        /*
         * 14. Save updated EMI
         */

        emiScheduleRepository.save(emi);

        /*
         * 15. Get applicant
         */

        User applicant = emi.getDisbursement()
                .getApplication()
                .getApplicant();

        /*
         * 16. Create Audit Log
         */

        AuditLog auditLog = new AuditLog();

        auditLog.setUser(applicant);

        auditLog.setApplication(
                emi.getDisbursement().getApplication());

        auditLog.setAction("PAYMENT_RECEIVED");

        auditLog.setDetails(
                "Payment of ₹"
                        + payment.getAmountPaid()
                        + " received for EMI #"
                        + emi.getInstallmentNo());

        auditLogService.createAuditLog(auditLog);

        /*
         * 17. Create Notification
         */

        Notification notification = new Notification();

        notification.setUser(applicant);

        notification.setType("PAYMENT");

        notification.setMessage(
                "Your EMI payment of ₹"
                        + payment.getAmountPaid()
                        + " has been received successfully.");

        notificationService.createNotification(notification);

        /*
         * 18. Return saved payment
         */

        return savedPayment;
    }
}