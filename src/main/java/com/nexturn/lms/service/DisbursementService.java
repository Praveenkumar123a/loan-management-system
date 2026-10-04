package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.Disbursement;

public interface DisbursementService {

    Disbursement createDisbursement(Disbursement disbursement);

    List<Disbursement> getAllDisbursements();

    Disbursement getDisbursementById(Integer disbursementId);

    Disbursement getDisbursementByApplication(Integer applicationId);

    Disbursement updateDisbursement(
            Integer disbursementId,
            Disbursement disbursement);

    void deleteDisbursement(Integer disbursementId);
}