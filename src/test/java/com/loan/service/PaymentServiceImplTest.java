package com.loan.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.Payment;
import com.nexturn.lms.entity.User;
import com.nexturn.lms.repository.EmiScheduleRepository;
import com.nexturn.lms.repository.PaymentRepository;
import com.nexturn.lms.service.AuditLogService;
import com.nexturn.lms.service.NotificationService;
import com.nexturn.lms.service.PaymentServiceImpl;
import com.nexturn.lms.utils.EmiStatus;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private EmiScheduleRepository emiScheduleRepository;

    @Mock
    private AuditLogService auditLogService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    void testMakePaymentSuccessfully() {

        User user = new User();

        LoanApplication application = new LoanApplication();
        application.setApplicant(user);

        Disbursement disbursement = new Disbursement();
        disbursement.setApplication(application);

        EmiSchedule emi = new EmiSchedule();
        emi.setEmiId(1);
        emi.setInstallmentNo(1);
        emi.setDueDate(LocalDate.now());
        emi.setEmiAmount(new BigDecimal("5000"));
        emi.setDisbursement(disbursement);
        emi.setStatus(EmiStatus.PENDING);

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(new BigDecimal("5000"));
        payment.setPaymentMode("UPI");

        when(emiScheduleRepository.findById(1))
                .thenReturn(java.util.Optional.of(emi));

        when(paymentRepository.getTotalAmountPaidForEmi(1))
                .thenReturn(BigDecimal.ZERO);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.makePayment(payment);

        org.junit.jupiter.api.Assertions.assertSame(
                payment,
                result);

        org.junit.jupiter.api.Assertions.assertNotNull(
                result.getTransactionRef());

        org.junit.jupiter.api.Assertions.assertTrue(
                result.getTransactionRef().startsWith("TXN-"));

        verify(paymentRepository, times(1))
                .save(payment);

        verify(emiScheduleRepository, times(1))
                .save(emi);

        verify(auditLogService, times(1))
                .createAuditLog(any());

        verify(notificationService, times(1))
                .createNotification(any());
    }

    @Test
    void testMakePaymentWithInvalidAmount() {

        EmiSchedule emi = new EmiSchedule();
        emi.setEmiId(1);

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(BigDecimal.ZERO);

        when(emiScheduleRepository.findById(1))
                .thenReturn(java.util.Optional.of(emi));

        RuntimeException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        RuntimeException.class,
                        () -> paymentService.makePayment(payment));

        org.junit.jupiter.api.Assertions.assertEquals(
                "Payment amount must be greater than zero",
                exception.getMessage());

        verify(paymentRepository, never())
                .save(any(Payment.class));
    }

    @Test
    void testMakePaymentGeneratesTransactionReference() {

        User user = new User();

        LoanApplication application = new LoanApplication();
        application.setApplicant(user);

        Disbursement disbursement = new Disbursement();
        disbursement.setApplication(application);

        EmiSchedule emi = new EmiSchedule();
        emi.setEmiId(1);
        emi.setInstallmentNo(1);
        emi.setDueDate(LocalDate.now());
        emi.setEmiAmount(new BigDecimal("5000"));
        emi.setDisbursement(disbursement);
        emi.setStatus(EmiStatus.PENDING);

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(new BigDecimal("5000"));
        payment.setPaymentMode("UPI");

        when(emiScheduleRepository.findById(1))
                .thenReturn(java.util.Optional.of(emi));

        when(paymentRepository.getTotalAmountPaidForEmi(1))
                .thenReturn(BigDecimal.ZERO);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.makePayment(payment);

        org.junit.jupiter.api.Assertions.assertNotNull(
                result.getTransactionRef());

        org.junit.jupiter.api.Assertions.assertTrue(
                result.getTransactionRef().startsWith("TXN-"));

        verify(paymentRepository, times(1))
                .save(payment);
    }

    @Test
    void testMakePaymentWithLatePenalty() {

        User user = new User();

        LoanApplication application = new LoanApplication();
        application.setApplicant(user);

        Disbursement disbursement = new Disbursement();
        disbursement.setApplication(application);

        EmiSchedule emi = new EmiSchedule();
        emi.setEmiId(1);
        emi.setInstallmentNo(1);
        emi.setDueDate(LocalDate.now().minusDays(2));
        emi.setEmiAmount(new BigDecimal("5000"));
        emi.setDisbursement(disbursement);
        emi.setStatus(EmiStatus.PENDING);

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(new BigDecimal("5200"));
        payment.setPaymentMode("UPI");
        payment.setPaymentDate(LocalDateTime.now());

        when(emiScheduleRepository.findById(1))
                .thenReturn(java.util.Optional.of(emi));

        when(paymentRepository.getTotalAmountPaidForEmi(1))
                .thenReturn(BigDecimal.ZERO);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.makePayment(payment);

        org.junit.jupiter.api.Assertions.assertEquals(
                new BigDecimal("200"),
                payment.getPenaltyPaid());

        org.junit.jupiter.api.Assertions.assertEquals(
                new BigDecimal("200"),
                emi.getPenaltyAmount());

        org.junit.jupiter.api.Assertions.assertNotNull(
                payment.getTransactionRef());

        org.junit.jupiter.api.Assertions.assertTrue(
                payment.getTransactionRef().startsWith("TXN-"));
    }

    @Test
    void testEmiBecomesPaidAfterFullPayment() {

        User user = new User();

        LoanApplication application = new LoanApplication();
        application.setApplicant(user);

        Disbursement disbursement = new Disbursement();
        disbursement.setApplication(application);

        EmiSchedule emi = new EmiSchedule();
        emi.setEmiId(1);
        emi.setInstallmentNo(1);
        emi.setDueDate(LocalDate.now());
        emi.setEmiAmount(new BigDecimal("5000"));
        emi.setDisbursement(disbursement);
        emi.setStatus(EmiStatus.PENDING);

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(new BigDecimal("5000"));
        payment.setPaymentMode("UPI");
        payment.setPaymentDate(LocalDateTime.now());

        when(emiScheduleRepository.findById(1))
                .thenReturn(java.util.Optional.of(emi));

        when(paymentRepository.getTotalAmountPaidForEmi(1))
                .thenReturn(BigDecimal.ZERO);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.makePayment(payment);

        org.junit.jupiter.api.Assertions.assertEquals(
                EmiStatus.PAID,
                emi.getStatus());

        org.junit.jupiter.api.Assertions.assertEquals(
                LocalDate.now(),
                emi.getPaidDate());

        org.junit.jupiter.api.Assertions.assertNotNull(
                payment.getTransactionRef());

        verify(emiScheduleRepository, times(1))
                .save(emi);
    }

    @Test
    void testPartialPaymentDoesNotMarkEmiAsPaid() {

        User user = new User();

        LoanApplication application = new LoanApplication();
        application.setApplicant(user);

        Disbursement disbursement = new Disbursement();
        disbursement.setApplication(application);

        EmiSchedule emi = new EmiSchedule();
        emi.setEmiId(1);
        emi.setInstallmentNo(1);
        emi.setDueDate(LocalDate.now());
        emi.setEmiAmount(new BigDecimal("5000"));
        emi.setDisbursement(disbursement);
        emi.setStatus(EmiStatus.PENDING);

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(new BigDecimal("2000"));
        payment.setPaymentMode("UPI");
        payment.setPaymentDate(LocalDateTime.now());

        when(emiScheduleRepository.findById(1))
                .thenReturn(java.util.Optional.of(emi));

        when(paymentRepository.getTotalAmountPaidForEmi(1))
                .thenReturn(BigDecimal.ZERO);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.makePayment(payment);

        org.junit.jupiter.api.Assertions.assertEquals(
                EmiStatus.PENDING,
                emi.getStatus());

        org.junit.jupiter.api.Assertions.assertNotNull(
                payment.getTransactionRef());

        verify(emiScheduleRepository, times(1))
                .save(emi);
    }

    @Test
    void testMakePaymentWithNullPayment() {

        RuntimeException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        RuntimeException.class,
                        () -> paymentService.makePayment(null));

        org.junit.jupiter.api.Assertions.assertEquals(
                "Payment cannot be null",
                exception.getMessage());

        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(emiScheduleRepository);
        verifyNoInteractions(auditLogService);
        verifyNoInteractions(notificationService);
    }

    @Test
    void testMakePaymentWithoutEmiSchedule() {

        Payment payment = new Payment();
        payment.setAmountPaid(new BigDecimal("5000"));

        RuntimeException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        RuntimeException.class,
                        () -> paymentService.makePayment(payment));

        org.junit.jupiter.api.Assertions.assertEquals(
                "EMI schedule is required",
                exception.getMessage());

        verifyNoInteractions(paymentRepository);
        verifyNoInteractions(emiScheduleRepository);
        verifyNoInteractions(auditLogService);
        verifyNoInteractions(notificationService);
    }

    @Test
    void testMakePaymentWithEmiNotFound() {

        EmiSchedule emi = new EmiSchedule();
        emi.setEmiId(999);

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(new BigDecimal("5000"));

        when(emiScheduleRepository.findById(999))
                .thenReturn(java.util.Optional.empty());

        RuntimeException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        RuntimeException.class,
                        () -> paymentService.makePayment(payment));

        org.junit.jupiter.api.Assertions.assertEquals(
                "EMI not found",
                exception.getMessage());

        verify(emiScheduleRepository, times(1))
                .findById(999);

        verify(paymentRepository, never())
                .save(any(Payment.class));

        verifyNoInteractions(auditLogService);
        verifyNoInteractions(notificationService);
    }

    @Test
    void testGetPaymentById() {

        Payment payment = new Payment();

        when(paymentRepository.findById(1))
                .thenReturn(java.util.Optional.of(payment));

        Payment result = paymentService.getPaymentById(1);

        org.junit.jupiter.api.Assertions.assertSame(
                payment,
                result);

        verify(paymentRepository, times(1))
                .findById(1);
    }

    @Test
    void testGetPaymentByIdNotFound() {

        when(paymentRepository.findById(999))
                .thenReturn(java.util.Optional.empty());

        RuntimeException exception =
                org.junit.jupiter.api.Assertions.assertThrows(
                        RuntimeException.class,
                        () -> paymentService.getPaymentById(999));

        org.junit.jupiter.api.Assertions.assertEquals(
                "Payment not found",
                exception.getMessage());

        verify(paymentRepository, times(1))
                .findById(999);
    }

    @Test
    void testGetPaymentsByEmi() {

        Payment payment1 = new Payment();
        Payment payment2 = new Payment();

        java.util.List<Payment> payments =
                java.util.List.of(payment1, payment2);

        when(paymentRepository.findByEmiSchedule_EmiId(1))
                .thenReturn(payments);

        java.util.List<Payment> result =
                paymentService.getPaymentsByEmi(1);

        org.junit.jupiter.api.Assertions.assertEquals(
                payments,
                result);

        verify(paymentRepository, times(1))
                .findByEmiSchedule_EmiId(1);
    }

    @Test
    void testPaymentCreatesCorrectAuditLog() {

        User user = new User();

        LoanApplication application = new LoanApplication();
        application.setApplicant(user);

        Disbursement disbursement = new Disbursement();
        disbursement.setApplication(application);

        EmiSchedule emi = new EmiSchedule();
        emi.setEmiId(1);
        emi.setInstallmentNo(1);
        emi.setDueDate(LocalDate.now());
        emi.setEmiAmount(new BigDecimal("5000"));
        emi.setDisbursement(disbursement);
        emi.setStatus(EmiStatus.PENDING);

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(new BigDecimal("5000"));
        payment.setPaymentMode("UPI");
        payment.setPaymentDate(LocalDateTime.now());

        when(emiScheduleRepository.findById(1))
                .thenReturn(java.util.Optional.of(emi));

        when(paymentRepository.getTotalAmountPaidForEmi(1))
                .thenReturn(BigDecimal.ZERO);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.makePayment(payment);

        verify(auditLogService, times(1))
                .createAuditLog(argThat(auditLog ->
                        auditLog.getUser() == user
                                && auditLog.getApplication() == application
                                && auditLog.getAction().equals("PAYMENT_RECEIVED")
                                && auditLog.getDetails().contains("₹5000")
                                && auditLog.getDetails().contains("EMI #1")
                ));
    }

    @Test
    void testPaymentCreatesCorrectNotification() {

        User user = new User();

        LoanApplication application = new LoanApplication();
        application.setApplicant(user);

        Disbursement disbursement = new Disbursement();
        disbursement.setApplication(application);

        EmiSchedule emi = new EmiSchedule();
        emi.setEmiId(1);
        emi.setInstallmentNo(1);
        emi.setDueDate(LocalDate.now());
        emi.setEmiAmount(new BigDecimal("5000"));
        emi.setDisbursement(disbursement);
        emi.setStatus(EmiStatus.PENDING);

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(new BigDecimal("5000"));
        payment.setPaymentMode("UPI");
        payment.setPaymentDate(LocalDateTime.now());

        when(emiScheduleRepository.findById(1))
                .thenReturn(java.util.Optional.of(emi));

        when(paymentRepository.getTotalAmountPaidForEmi(1))
                .thenReturn(BigDecimal.ZERO);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.makePayment(payment);

        verify(notificationService, times(1))
                .createNotification(argThat(notification ->
                        notification.getUser() == user
                                && notification.getType().equals("PAYMENT")
                                && notification.getMessage().contains("₹5000")
                                && notification.getMessage().contains("received successfully")
                ));
    }

    @Test
    void testOnTimePaymentHasNoPenalty() {

        User user = new User();

        LoanApplication application = new LoanApplication();
        application.setApplicant(user);

        Disbursement disbursement = new Disbursement();
        disbursement.setApplication(application);

        LocalDate dueDate = LocalDate.now();

        EmiSchedule emi = new EmiSchedule();
        emi.setEmiId(1);
        emi.setInstallmentNo(1);
        emi.setDueDate(dueDate);
        emi.setEmiAmount(new BigDecimal("5000"));
        emi.setDisbursement(disbursement);
        emi.setStatus(EmiStatus.PENDING);

        Payment payment = new Payment();
        payment.setEmiSchedule(emi);
        payment.setAmountPaid(new BigDecimal("5000"));
        payment.setPaymentMode("UPI");
        payment.setPaymentDate(
                dueDate.atStartOfDay());

        when(emiScheduleRepository.findById(1))
                .thenReturn(java.util.Optional.of(emi));

        when(paymentRepository.getTotalAmountPaidForEmi(1))
                .thenReturn(BigDecimal.ZERO);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.makePayment(payment);

        org.junit.jupiter.api.Assertions.assertEquals(
                BigDecimal.ZERO,
                payment.getPenaltyPaid());

        org.junit.jupiter.api.Assertions.assertEquals(
                BigDecimal.ZERO,
                emi.getPenaltyAmount());

        org.junit.jupiter.api.Assertions.assertNotNull(
                payment.getTransactionRef());
    }
}