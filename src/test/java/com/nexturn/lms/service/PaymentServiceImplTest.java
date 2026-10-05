package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.entity.LoanApplication;
import com.nexturn.lms.entity.LoanProduct;
import com.nexturn.lms.entity.Payment;
import com.nexturn.lms.repository.EmiScheduleRepository;
import com.nexturn.lms.repository.PaymentRepository;
import com.nexturn.lms.utils.EmiStatus;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private EmiScheduleRepository emiScheduleRepository;

    @Mock
    private EmiScheduleService emiScheduleService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private EmiSchedule emi;
    private LoanProduct product;
    private LoanApplication application;
    private Disbursement disbursement;

    @BeforeEach
    void setUp() {

        product = new LoanProduct();

        application = new LoanApplication();
        application.setProduct(product);

        disbursement = new Disbursement();
        disbursement.setApplication(application);

        emi = new EmiSchedule();
        emi.setEmiId(1);
        emi.setEmiAmount(new BigDecimal("5000"));
        emi.setDueDate(LocalDate.now());
        emi.setStatus(EmiStatus.PENDING);
        emi.setDisbursement(disbursement);
    }

    @Test
    void payEmi_shouldCreatePaymentAndMarkEmiPaid() {

        when(emiScheduleService.getById(1))
                .thenReturn(emi);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result =
                paymentService.payEmi(
                        1,
                        "UPI",
                        "TXN-001"
                );

        assertNotNull(result);

        assertEquals(
                emi,
                result.getEmiSchedule()
        );

        assertEquals(
                new BigDecimal("5000"),
                result.getAmountPaid()
        );

        assertEquals(
                BigDecimal.ZERO,
                result.getPenaltyPaid()
        );

        assertEquals(
                "UPI",
                result.getPaymentMode()
        );

        assertEquals(
                "TXN-001",
                result.getTransactionRef()
        );

        assertNotNull(
                result.getPaymentDate()
        );

        assertEquals(
                BigDecimal.ZERO,
                emi.getPenaltyAmount()
        );

        assertEquals(
                EmiStatus.PAID,
                emi.getStatus()
        );

        assertEquals(
                LocalDate.now(),
                emi.getPaidDate()
        );

        verify(emiScheduleService)
                .getById(1);

        verify(paymentRepository)
                .save(any(Payment.class));

        verify(emiScheduleRepository)
                .save(emi);
    }

    @Test
    void payEmi_shouldApplyPercentagePenaltyForOverdueEmi() {

        emi.setDueDate(
                LocalDate.now().minusDays(5)
        );

        product.setPenaltyType("PERCENT");
        product.setPenaltyValue(
                new BigDecimal("2")
        );

        when(emiScheduleService.getById(1))
                .thenReturn(emi);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result =
                paymentService.payEmi(
                        1,
                        "CARD",
                        "TXN-PERCENT-001"
                );

        assertNotNull(result);

        // 5000 * 2 / 100 = 100
        assertEquals(
                new BigDecimal("100.00"),
                result.getPenaltyPaid()
        );

        assertEquals(
                new BigDecimal("100.00"),
                emi.getPenaltyAmount()
        );

        assertEquals(
                EmiStatus.PAID,
                emi.getStatus()
        );

        verify(paymentRepository)
                .save(any(Payment.class));

        verify(emiScheduleRepository)
                .save(emi);
    }

    @Test
    void payEmi_shouldApplyFixedPenaltyForOverdueEmi() {

        emi.setDueDate(
                LocalDate.now().minusDays(3)
        );

        product.setPenaltyType("FIXED");
        product.setPenaltyValue(
                new BigDecimal("250")
        );

        when(emiScheduleService.getById(1))
                .thenReturn(emi);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result =
                paymentService.payEmi(
                        1,
                        "NET_BANKING",
                        "TXN-FIXED-001"
                );

        assertNotNull(result);

        assertEquals(
                new BigDecimal("250"),
                result.getPenaltyPaid()
        );

        assertEquals(
                new BigDecimal("250"),
                emi.getPenaltyAmount()
        );

        assertEquals(
                EmiStatus.PAID,
                emi.getStatus()
        );

        verify(paymentRepository)
                .save(any(Payment.class));

        verify(emiScheduleRepository)
                .save(emi);
    }

    @Test
    void payEmi_shouldSetPaymentDetailsCorrectly() {

        when(emiScheduleService.getById(1))
                .thenReturn(emi);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Payment result =
                paymentService.payEmi(
                        1,
                        "UPI",
                        "TXN-DETAIL-001"
                );

        assertEquals(
                1,
                result.getEmiSchedule().getEmiId()
        );

        assertEquals(
                new BigDecimal("5000"),
                result.getAmountPaid()
        );

        assertEquals(
                "UPI",
                result.getPaymentMode()
        );

        assertEquals(
                "TXN-DETAIL-001",
                result.getTransactionRef()
        );

        assertNotNull(
                result.getPaymentDate()
        );
    }

    @Test
    void payEmi_shouldSaveUpdatedEmi() {

        when(emiScheduleService.getById(1))
                .thenReturn(emi);

        when(paymentRepository.save(any(Payment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        paymentService.payEmi(
                1,
                "UPI",
                "TXN-UPDATE-001"
        );

        ArgumentCaptor<EmiSchedule> captor =
                ArgumentCaptor.forClass(EmiSchedule.class);

        verify(emiScheduleRepository)
                .save(captor.capture());

        EmiSchedule savedEmi =
                captor.getValue();

        assertEquals(
                EmiStatus.PAID,
                savedEmi.getStatus()
        );

        assertEquals(
                BigDecimal.ZERO,
                savedEmi.getPenaltyAmount()
        );

        assertEquals(
                LocalDate.now(),
                savedEmi.getPaidDate()
        );
    }
}