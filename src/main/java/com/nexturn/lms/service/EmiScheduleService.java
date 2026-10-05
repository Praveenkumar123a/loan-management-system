package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.Disbursement;
import com.nexturn.lms.entity.EmiSchedule;

public interface EmiScheduleService {
    List<EmiSchedule> getByDisbursement(Disbursement disbursement);
    List<EmiSchedule> getOverdueEmis();
    EmiSchedule getById(Integer emiId);
}