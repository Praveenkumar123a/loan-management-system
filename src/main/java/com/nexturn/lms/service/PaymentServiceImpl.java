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
import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.entity.Notification;
import com.nexturn.lms.entity.Payment;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.exception.EmiScheduleNotFoundException;
import com.nexturn.lms.exception.InvalidPaymentException;
import com.nexturn.lms.exception.PaymentNotFoundException;
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
                .orElseThrow(() ->
                        new PaymentNotFoundException("Payment not found"));
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
            throw new InvalidPaymentException(
                    "Payment cannot be null");
        }

        // 2. Validate EMI schedule
        if (payment.getEmiSchedule() == null
                || payment.getEmiSchedule().getEmiId() == null) {

            throw new InvalidPaymentException(
                    "EMI schedule is required");
        }

        Integer emiId = payment.getEmiSchedule().getEmiId();

        // 3. Find EMI
        EmiSchedule emi = emiScheduleRepository.findById(emiId)
                .orElseThrow(() ->
                        new EmiScheduleNotFoundException(
                                "EMI schedule not found"));

        // 4. Validate payment amount
        if (payment.getAmountPaid() == null
                || payment.getAmountPaid().signum() <= 0) {

            throw new InvalidPaymentException(
                    "Payment amount must be greater than zero");
        }

        // 5. Validate EMI amount
        if (emi.getEmiAmount() == null) {
            throw new InvalidPaymentException(
                    "EMI amount is not configured");
        }

        // 6. Generate transaction reference
        String transactionRef = "TXN-" + UUID.randomUUID();

        payment.setTransactionRef(transactionRef);

        // 7. Set payment date if not provided
        if (payment.getPaymentDate() == null) {
            payment.setPaymentDate(LocalDateTime.now());
        }

        /*
         * 8. Calculate late payment penalty
         */

        BigDecimal penalty = BigDecimal.ZERO;

        long lateDays = ChronoUnit.DAYS.between(
                emi.getDueDate(),
                payment.getPaymentDate().toLocalDate());

        if (lateDays > 0) {

            LoanProduct loanProduct = emi.getDisbursement()
                    .getApplication()
                    .getProduct();

            if (loanProduct == null) {
                throw new InvalidPaymentException(
                        "Loan product is not configured");
            }

            if (loanProduct.getPenaltyType() == null
                    || loanProduct.getPenaltyValue() == null) {

                throw new InvalidPaymentException(
                        "Penalty policy is not configured");
            }

            String penaltyType =
                    loanProduct.getPenaltyType().trim().toUpperCase();

            BigDecimal penaltyValue =
                    loanProduct.getPenaltyValue();

            if (penaltyValue.signum() < 0) {
                throw new InvalidPaymentException(
                        "Penalty value cannot be negative");
            }

            if ("DAILY".equals(penaltyType)) {

                penalty = penaltyValue.multiply(
                        BigDecimal.valueOf(lateDays));

            } else if ("PERCENTAGE".equals(penaltyType)) {

                penalty = emi.getEmiAmount()
                        .multiply(penaltyValue)
                        .divide(
                                BigDecimal.valueOf(100));

            } else {

                throw new InvalidPaymentException(
                        "Unsupported penalty type: "
                                + loanProduct.getPenaltyType());
            }
        }

        payment.setPenaltyPaid(penalty);
        emi.setPenaltyAmount(penalty);

        /*
         * 9. Calculate previous amount paid
         */

        BigDecimal previousPaid =
                paymentRepository.getTotalAmountPaidForEmi(emiId);

        if (previousPaid == null) {
            previousPaid = BigDecimal.ZERO;
        }

        /*
         * 10. Calculate total amount paid
         */

        BigDecimal totalPaid =
                previousPaid.add(payment.getAmountPaid());

        /*
         * 11. Calculate total amount due
         */

        BigDecimal totalDue =
                emi.getEmiAmount().add(penalty);

        /*
         * 12. Update outstanding balance
         */

        BigDecimal currentOutstanding =
                emi.getOutstandingBalance();

        if (currentOutstanding == null) {
            currentOutstanding = emi.getEmiAmount();
        }

        BigDecimal newOutstanding =
                currentOutstanding.subtract(
                        payment.getAmountPaid());

        if (newOutstanding.signum() < 0) {
            newOutstanding = BigDecimal.ZERO;
        }

        emi.setOutstandingBalance(newOutstanding);

        /*
         * 13. Check whether EMI is completely paid
         */

        if (totalPaid.compareTo(totalDue) >= 0) {

            emi.setStatus(EmiStatus.PAID);

            emi.setPaidDate(
                    payment.getPaymentDate().toLocalDate());
        }

        /*
         * 14. Attach updated EMI to payment
         */

        payment.setEmiSchedule(emi);

        /*
         * 15. Save updated EMI
         */

        emiScheduleRepository.save(emi);

        /*
         * 16. Save payment
         */

        Payment savedPayment =
                paymentRepository.save(payment);

        /*
         * 17. Get applicant
         */

        User applicant = emi.getDisbursement()
                .getApplication()
                .getApplicant();

        /*
         * 18. Create Audit Log
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
         * 19. Create Notification
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
         * 20. Return saved payment
         */

        return savedPayment;
    }
}