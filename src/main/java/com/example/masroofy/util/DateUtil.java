package com.example.masroofy.util;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Utility class for date-related operations in the Masroofy application.
 * <p>
 * The {@code DateUtil} class provides static helper methods for common date
 * manipulation tasks, particularly those involving timestamp comparisons
 * and date calculations.
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Checking if a given timestamp falls on today's date</li>
 * </ul>
 *
 * <p><b>Usage Examples:</b></p>
 * <pre>
 * // Check if a transaction occurred today
 * long transactionTime = transaction.getTransactionTimestamp();
 * if (DateUtil.isToday(transactionTime)) {
 *     // Adjust today's daily limit
 * }
 *
 * // Check if a user's last activity was today
 * long lastActivity = user.getLastActivityTimestamp();
 * boolean wasToday = DateUtil.isToday(lastActivity);
 * </pre>
 *
 * <p><b>Important Notes:</b></p>
 * <ul>
 *   <li>All methods are static - no instantiation needed</li>
 *   <li>Uses the system's default time zone for date calculations</li>
 *   <li>Timestamps are expected to be in milliseconds since Unix epoch</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see java.time.LocalDate
 * @see java.time.Instant
 * @see com.example.masroofy.Model.History
 * @see com.example.masroofy.Model.QuickEntry
 */
public class DateUtil {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This is a utility class with only static methods, so it should not be
     * instantiated.
     * </p>
     */
    private DateUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Determines whether a given timestamp represents a date that is today.
     * <p>
     * This method converts the provided timestamp to a {@link LocalDate} using
     * the system's default time zone and compares it with today's date.
     * </p>
     *
     * <p><b>Use Cases:</b></p>
     * <ul>
     *   <li>Determining if a transaction needs to affect today's daily limit</li>
     *   <li>Checking if a daily limit recalculation is needed</li>
     *   <li>Filtering transactions that occurred today</li>
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre>
     * // Check if a transaction is from today
     * Transaction t = getTransaction();
     * if (DateUtil.isToday(t.getTransactionTimestamp())) {
     *     // This transaction affects today's daily limit
     *     dailyLimit -= t.getTransactionAmount();
     * }
     * </pre>
     *
     * @param timestamp the timestamp in milliseconds since Unix epoch
     *                  (e.g., from {@link System#currentTimeMillis()})
     * @return {@code true} if the timestamp's date is today,
     *         {@code false} otherwise
     *
     * @see System#currentTimeMillis()
     * @see LocalDate#now()
     * @see LocalDate#equals(Object)
     */
    public static boolean isToday(long timestamp) {
        LocalDate date = LocalDate.ofInstant(
                java.time.Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
        );
        return date.equals(LocalDate.now());
    }
}