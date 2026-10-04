package com.nexturn.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.exception.EmiScheduleNotFoundException;
import com.nexturn.lms.repository.EmiScheduleRepository;

@Service
public class EmiScheduleServiceImpl implements EmiScheduleService {

    private final EmiScheduleRepository emiScheduleRepository;

    public EmiScheduleServiceImpl(
            EmiScheduleRepository emiScheduleRepository) {

        this.emiScheduleRepository = emiScheduleRepository;
    }

    @Override
    public EmiSchedule createEmiSchedule(
            EmiSchedule emiSchedule) {

        if (emiSchedule == null) {
            throw new IllegalArgumentException(
                    "EMI schedule cannot be null");
        }

        return emiScheduleRepository.save(emiSchedule);
    }

    @Override
    public List<EmiSchedule> getAllEmiSchedules() {

        return emiScheduleRepository.findAll();
    }

    @Override
    public EmiSchedule getEmiScheduleById(
            Integer emiId) {

        return emiScheduleRepository.findById(emiId)
                .orElseThrow(() ->
                        new EmiScheduleNotFoundException(
                                "EMI schedule not found"));
    }

    @Override
    public List<EmiSchedule> getEmiSchedulesByDisbursement(
            Integer disbursementId) {

        return emiScheduleRepository.findAll()
                .stream()
                .filter(emi ->
                        emi.getDisbursement() != null
                        && emi.getDisbursement()
                                .getDisbursementId() == disbursementId)
                .toList();
    }

    @Override
    public EmiSchedule updateEmiSchedule(
            Integer emiId,
            EmiSchedule emiSchedule) {

        EmiSchedule existingEmi =
                emiScheduleRepository.findById(emiId)
                        .orElseThrow(() ->
                                new EmiScheduleNotFoundException(
                                        "EMI schedule not found"));

        existingEmi.setDisbursement(
                emiSchedule.getDisbursement());

        existingEmi.setInstallmentNo(
                emiSchedule.getInstallmentNo());

        existingEmi.setDueDate(
                emiSchedule.getDueDate());

        existingEmi.setEmiAmount(
                emiSchedule.getEmiAmount());

        existingEmi.setPrincipalComponent(
                emiSchedule.getPrincipalComponent());

        existingEmi.setInterestComponent(
                emiSchedule.getInterestComponent());

        existingEmi.setOutstandingBalance(
                emiSchedule.getOutstandingBalance());

        existingEmi.setPenaltyAmount(
                emiSchedule.getPenaltyAmount());

        existingEmi.setStatus(
                emiSchedule.getStatus());

        existingEmi.setPaidDate(
                emiSchedule.getPaidDate());

        return emiScheduleRepository.save(existingEmi);
    }

    @Override
    public void deleteEmiSchedule(Integer emiId) {

        EmiSchedule existingEmi =
                emiScheduleRepository.findById(emiId)
                        .orElseThrow(() ->
                                new EmiScheduleNotFoundException(
                                        "EMI schedule not found"));

        emiScheduleRepository.delete(existingEmi);
    }
}