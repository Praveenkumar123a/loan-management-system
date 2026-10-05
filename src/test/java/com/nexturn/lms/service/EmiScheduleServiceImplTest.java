package com.nexturn.lms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.EmiScheduleRepository;
import com.nexturn.lms.utils.EmiStatus;

@ExtendWith(MockitoExtension.class)
class EmiScheduleServiceImplTest {

    @Mock
    private EmiScheduleRepository emiScheduleRepository;

    @InjectMocks
    private EmiScheduleServiceImpl emiScheduleService;

    private Disbursement disbursement;

    @BeforeEach
    void setUp() {

        disbursement = new Disbursement();

        disbursement.setDisbursementId(1);
    }

    @Test
    void getByDisbursement_shouldReturnEmiList() {

        EmiSchedule emi1 = new EmiSchedule();
        emi1.setEmiId(1);
        emi1.setInstallmentNo(1);
        emi1.setStatus(EmiStatus.PENDING);

        EmiSchedule emi2 = new EmiSchedule();
        emi2.setEmiId(2);
        emi2.setInstallmentNo(2);
        emi2.setStatus(EmiStatus.PENDING);

        List<EmiSchedule> emis =
                List.of(emi1, emi2);

        when(emiScheduleRepository
                .findByDisbursementOrderByInstallmentNoAsc(disbursement))
                .thenReturn(emis);

        List<EmiSchedule> result =
                emiScheduleService.getByDisbursement(
                        disbursement
                );

        assertNotNull(result);

        assertEquals(
                2,
                result.size()
        );

        assertEquals(
                emi1,
                result.get(0)
        );

        assertEquals(
                emi2,
                result.get(1)
        );

        verify(emiScheduleRepository)
                .findByDisbursementOrderByInstallmentNoAsc(
                        disbursement
                );
    }

    @Test
    void getByDisbursement_shouldReturnEmptyListWhenNoEmisExist() {

        when(emiScheduleRepository
                .findByDisbursementOrderByInstallmentNoAsc(disbursement))
                .thenReturn(List.of());

        List<EmiSchedule> result =
                emiScheduleService.getByDisbursement(
                        disbursement
                );

        assertNotNull(result);

        assertEquals(
                0,
                result.size()
        );

        verify(emiScheduleRepository)
                .findByDisbursementOrderByInstallmentNoAsc(
                        disbursement
                );
    }

    @Test
    void getOverdueEmis_shouldReturnPendingOverdueEmis() {

        EmiSchedule overdueEmi =
                new EmiSchedule();

        overdueEmi.setEmiId(1);
        overdueEmi.setInstallmentNo(1);
        overdueEmi.setDueDate(
                LocalDate.now().minusDays(5)
        );
        overdueEmi.setStatus(
                EmiStatus.PENDING
        );

        when(emiScheduleRepository
                .findByDueDateBeforeAndStatus(
                        LocalDate.now(),
                        EmiStatus.PENDING
                ))
                .thenReturn(List.of(overdueEmi));

        List<EmiSchedule> result =
                emiScheduleService.getOverdueEmis();

        assertNotNull(result);

        assertEquals(
                1,
                result.size()
        );

        assertEquals(
                overdueEmi,
                result.get(0)
        );

        verify(emiScheduleRepository)
                .findByDueDateBeforeAndStatus(
                        LocalDate.now(),
                        EmiStatus.PENDING
                );
    }

    @Test
    void getOverdueEmis_shouldReturnEmptyListWhenNoOverdueEmisExist() {

        when(emiScheduleRepository
                .findByDueDateBeforeAndStatus(
                        LocalDate.now(),
                        EmiStatus.PENDING
                ))
                .thenReturn(List.of());

        List<EmiSchedule> result =
                emiScheduleService.getOverdueEmis();

        assertNotNull(result);

        assertEquals(
                0,
                result.size()
        );

        verify(emiScheduleRepository)
                .findByDueDateBeforeAndStatus(
                        LocalDate.now(),
                        EmiStatus.PENDING
                );
    }

    @Test
    void getById_shouldReturnEmi() {

        EmiSchedule emi =
                new EmiSchedule();

        emi.setEmiId(1);
        emi.setInstallmentNo(1);
        emi.setStatus(EmiStatus.PENDING);

        when(emiScheduleRepository.findById(1))
                .thenReturn(Optional.of(emi));

        EmiSchedule result =
                emiScheduleService.getById(1);

        assertNotNull(result);

        assertEquals(
                emi,
                result
        );

        assertEquals(
                1,
                result.getEmiId()
        );

        verify(emiScheduleRepository)
                .findById(1);
    }

    @Test
    void getById_shouldThrowExceptionWhenEmiDoesNotExist() {

        when(emiScheduleRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> emiScheduleService.getById(999)
        );

        verify(emiScheduleRepository)
                .findById(999);
    }
}