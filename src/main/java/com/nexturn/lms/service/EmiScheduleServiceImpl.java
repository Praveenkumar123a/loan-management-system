package com.nexturn.lms.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.exception.ResourceNotFoundException;
import com.nexturn.lms.repository.EmiScheduleRepository;

import com.nexturn.lms.utils.EmiStatus;

@Service
public class EmiScheduleServiceImpl implements EmiScheduleService {

    @Autowired
    private EmiScheduleRepository emiScheduleRepository;

    @Override
    public List<EmiSchedule> getByDisbursement(Disbursement disbursement) {
        return emiScheduleRepository.findByDisbursementOrderByInstallmentNoAsc(disbursement);
    }

    @Override
    public List<EmiSchedule> getOverdueEmis() {
        
        return emiScheduleRepository.findByDueDateBeforeAndStatus(LocalDate.now(), EmiStatus.PENDING);
    }

    @Override
    public EmiSchedule getById(Integer emiId) {
        return emiScheduleRepository.findById(emiId)
                .orElseThrow(() -> new ResourceNotFoundException("EMI not found: " + emiId));
    }
}