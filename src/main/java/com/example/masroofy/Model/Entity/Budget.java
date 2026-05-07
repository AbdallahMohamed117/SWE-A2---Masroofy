package com.example.masroofy.Model.Entity;

import java.util.Date;
import java.time.*;

/**
 * Represents a budget cycle in the Masroofy application.
 * <p>
 * The {@code Budget} class encapsulates all financial parameters for a budget cycle,
 * including the total allowance, date range, and daily spending limits. It provides
 * functionality to calculate the daily safe limit based on the allowance and the
 * number of days in the budget period.
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Stores total allowance amount for the budget cycle</li>
 *   <li>Tracks start and end dates of the budget period</li>
 *   <li>Calculates daily safe spending limit automatically</li>
 *   <li>Maintains original daily limit for reference</li>
 *   <li>Tracks the last recalculation date for limit adjustments</li>
 * </ul>
 *
 * <p><b>Daily Safe Limit Calculation:</b></p>
 * The daily safe limit is calculated as:
 * <pre>dailySafeLimit = allowance / daysBetween(startDate, endDate)</pre>
 * This represents the maximum amount that can be spent per day to stay within
 * the total allowance over the entire budget period.
 *
 * @version 1.0
 * @since 1.0
 * @see Transaction
 * @see com.example.masroofy.Model.Setup
 */
public class Budget {

    /** The total allowance amount for the budget cycle. */
    private double allowance;

    /** The start date of the budget cycle. */
    private Date startDate;

    /** The end date of the budget cycle. */
    private Date endDate;

    /** The calculated daily safe spending limit. */
    private double dailySafeLimit;

    /** The original daily limit value (for reference when recalculating). */
    private double originalDailyLimit;

    /** The date when the daily limit was last recalculated. */
    private Date lastRecalcDate;

    /**
     * Returns the total allowance amount.
     *
     * @return the allowance amount
     */
    public double getAllowance() {
        return allowance;
    }

    /**
     * Sets the total allowance amount.
     *
     * @param allowance the allowance amount to set
     */
    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    /**
     * Returns the start date of the budget cycle.
     *
     * @return the start date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Sets the start date of the budget cycle.
     *
     * @param startDate the start date to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Returns the end date of the budget cycle.
     *
     * @return the end date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the end date of the budget cycle.
     *
     * @param endDate the end date to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Returns the daily safe spending limit.
     * <p>
     * This value represents the maximum amount that can be spent per day
     * to stay within the total allowance over the entire budget period.
     * </p>
     *
     * @return the daily safe limit
     */
    public double getDailySafeLimit() {
        return dailySafeLimit;
    }

    /**
     * Calculates and sets the daily safe spending limit.
     * <p>
     * This method computes the number of days between the start and end dates,
     * then divides the total allowance by that number to determine the daily
     * safe spending limit.
     * </p>
     *
     * <p><b>Calculation Formula:</b></p>
     * <pre>
     * daysBetween = endDate - startDate (in days)
     * dailySafeLimit = allowance / daysBetween
     * </pre>
     *
     * <p><b>Note:</b></p>
     * If the budget period is zero days, this calculation would result in
     * division by zero. The calling code should ensure valid date ranges
     * (start date before end date) before invoking this method.
     */
    public void setDailysafeLimit() {
        LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(start, end);

        this.dailySafeLimit = allowance / daysBetween;
    }

    /**
     * Returns the original daily limit value.
     * <p>
     * This value can be used to reset the daily limit after temporary
     * adjustments, or to track how the limit has changed over time.
     * </p>
     *
     * @return the original daily limit
     */
    public double getOriginalDailyLimit() {
        return originalDailyLimit;
    }

    /**
     * Sets the original daily limit value.
     *
     * @param originalDailyLimit the original daily limit to set
     */
    public void setOriginalDailyLimit(double originalDailyLimit) {
        this.originalDailyLimit = originalDailyLimit;
    }

    /**
     * Returns the date when the daily limit was last recalculated.
     *
     * @return the last recalculation date
     */
    public Date getLastRecalcDate() {
        return lastRecalcDate;
    }

    /**
     * Sets the date when the daily limit was last recalculated.
     *
     * @param lastRecalcDate the last recalculation date to set
     */
    public void setLastRecalcDate(Date lastRecalcDate) {
        this.lastRecalcDate = lastRecalcDate;
    }
}