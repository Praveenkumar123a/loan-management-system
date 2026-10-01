package com.nexturn.loan.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexturn.loan.entity.EmiSchedule;

@Repository 
public interface EmiScheduleRepository extends JpaRepository<EmiSchedule,Integer>{

}
