package com.example.masroofy.Model.Entity;

import java.util.Date;
import java.time.*;


public class Budget {
    private double allowance;
    private Date startDate;
    private Date endDate;
    private double dailySafeLimit;


    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public double getDailySafeLimit() {
        return dailySafeLimit;
    }

    public void setDailysafeLimit(double dailysafeLimit) {
        //needs full implementation !!!!!!!

        //to get the number of days
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(start, end);

        this.dailySafeLimit = allowance / daysBetween;
    }
}