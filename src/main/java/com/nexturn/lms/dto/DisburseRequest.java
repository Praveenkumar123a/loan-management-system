package com.nexturn.lms.dto;

import java.time.LocalDate;

public class DisburseRequest {
    private Integer disbursedById;
    private LocalDate disbursedDate;

    public Integer getDisbursedById() { return disbursedById; }
    public void setDisbursedById(Integer disbursedById) { this.disbursedById = disbursedById; }
    public LocalDate getDisbursedDate() { return disbursedDate; }
    public void setDisbursedDate(LocalDate disbursedDate) { this.disbursedDate = disbursedDate; }
}