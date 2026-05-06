package com.example.masroofy.Model.Entity;

import java.util.Date;
import java.time.*;


public class Budget {
    private double allowance;
    private Date startDate;
    private Date endDate;
    private double dailySafeLimit;
    private double originalDailyLimit;
    private Date lastRecalcDate;


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

    public void setDailysafeLimit() {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(start, end);

        this.dailySafeLimit = allowance / daysBetween;
    }

    public double getOriginalDailyLimit() {
        return originalDailyLimit;
    }

    public void setOriginalDailyLimit(double originalDailyLimit) {
        this.originalDailyLimit = originalDailyLimit;
    }

    public Date getLastRecalcDate() {
        return lastRecalcDate;
    }

    public void setLastRecalcDate(Date lastRecalcDate) {
        this.lastRecalcDate = lastRecalcDate;
    }
}