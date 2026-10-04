package com.nexturn.lms.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.repository.DisbursementRepository;

@Service
public class DisbursementServiceImpl implements DisbursementService {

    private final DisbursementRepository disbursementRepository;

    public DisbursementServiceImpl(
            DisbursementRepository disbursementRepository) {

        this.disbursementRepository = disbursementRepository;
    }

    @Override
    public Disbursement createDisbursement(
            Disbursement disbursement) {

        if (disbursement == null) {
            throw new IllegalArgumentException(
                    "Disbursement cannot be null");
        }

        return disbursementRepository.save(disbursement);
    }

    @Override
    public List<Disbursement> getAllDisbursements() {

        return disbursementRepository.findAll();
    }

    @Override
    public Disbursement getDisbursementById(
            Integer disbursementId) {

        return disbursementRepository.findById(disbursementId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Disbursement not found"));
    }

    @Override
    public Disbursement getDisbursementByApplication(
            Integer applicationId) {

        return disbursementRepository.findAll()
                .stream()
                .filter(disbursement ->
                        disbursement.getApplication() != null
                        && disbursement.getApplication()
                                .getApplicationId() == applicationId)
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Disbursement not found"));
    }

    @Override
    public Disbursement updateDisbursement(
            Integer disbursementId,
            Disbursement disbursement) {

        Disbursement existingDisbursement =
                disbursementRepository.findById(disbursementId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Disbursement not found"));

        existingDisbursement.setApplication(
                disbursement.getApplication());

        existingDisbursement.setDisbursedAmount(
                disbursement.getDisbursedAmount());

        existingDisbursement.setDisbursedDate(
                disbursement.getDisbursedDate());

        existingDisbursement.setDisbursedBy(
                disbursement.getDisbursedBy());

        return disbursementRepository.save(
                existingDisbursement);
    }

    @Override
    public void deleteDisbursement(
            Integer disbursementId) {

        Disbursement existingDisbursement =
                disbursementRepository.findById(disbursementId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Disbursement not found"));

        disbursementRepository.delete(
                existingDisbursement);
    }
}