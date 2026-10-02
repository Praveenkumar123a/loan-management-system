package com.nexturn.lms.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexturn.lms.entity.EmiSchedule;
import com.nexturn.lms.utils.EmiStatus;

public interface EmiScheduleRepository
        extends JpaRepository<EmiSchedule, Integer> {

    List<EmiSchedule> findByDueDateAndStatus(
            LocalDate dueDate,
            EmiStatus status
    );

    List<EmiSchedule> findByDueDateBeforeAndStatus(
        LocalDate date,
        EmiStatus status
);
}
