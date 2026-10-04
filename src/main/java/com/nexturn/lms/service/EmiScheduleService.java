package com.nexturn.lms.service;

import java.util.List;

import com.nexturn.lms.entity.EmiSchedule;

public interface EmiScheduleService {

    EmiSchedule createEmiSchedule(EmiSchedule emiSchedule);

    List<EmiSchedule> getAllEmiSchedules();

    EmiSchedule getEmiScheduleById(Integer emiId);

    List<EmiSchedule> getEmiSchedulesByDisbursement(
            Integer disbursementId);

    EmiSchedule updateEmiSchedule(
            Integer emiId,
            EmiSchedule emiSchedule);

    void deleteEmiSchedule(Integer emiId);
}