package com.example.masroofy.Listener;

import java.util.Date;

/**
 * Listener interface for handling setup form submission events.
 * <p>
 * The {@code SetupListener} interface defines a callback method for when a user
 * completes the first-time setup form. Implementations (typically the
 * {@code SetupController}) validate the input data and create a new budget cycle.
 * </p>
 *
 * <p><b>Setup Form Fields:</b></p>
 * <ul>
 *   <li><b>Allowance:</b> The total budget amount for the cycle</li>
 *   <li><b>Start Date:</b> The beginning date of the budget cycle</li>
 *   <li><b>End Date:</b> The ending date of the budget cycle</li>
 * </ul>
 *
 * <p><b>Validation Rules (implemented by controller):</b></p>
 * <ul>
 *   <li>Allowance must be non-negative (≥ 0)</li>
 *   <li>Start date cannot be after end date</li>
 * </ul>
 *
 * <p><b>On Successful Submission:</b></p>
 * <ul>
 *   <li>A new {@code Budget} object is created with the submitted values</li>
 *   <li>The daily safe spending limit is calculated automatically</li>
 *   <li>The budget cycle is saved to the database</li>
 *   <li>The user is navigated to the PIN setup screen</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see com.example.masroofy.Controller.SetupController
 * @see com.example.masroofy.View.SetupView
 * @see com.example.masroofy.Model.Setup
 * @see com.example.masroofy.Model.Entity.Budget
 */
public interface SetupListener {

    /**
     * Called when the setup form is submitted by the user.
     * <p>
     * This method is triggered when the user has entered their allowance amount
     * and selected start and end dates, then clicks the submit button to complete
     * the first-time application setup.
     * </p>
     *
     * <p><b>Implementation Responsibilities:</b></p>
     * <ul>
     *   <li>Validate that allowance is not negative</li>
     *   <li>Validate that start date is not after end date</li>
     *   <li>Display appropriate error messages for invalid input</li>
     *   <li>If validation passes, create a new {@code Budget} object</li>
     *   <li>Set allowance, start date, and end date on the budget</li>
     *   <li>Calculate the daily safe spending limit</li>
     *   <li>Save the budget cycle to the model</li>
     *   <li>Navigate to the next screen (PIN setup)</li>
     * </ul>
     *
     * <p><b>Return Value Interpretation:</b></p>
     * <ul>
     *   <li>{@code true} - Validation passed, budget cycle created successfully</li>
     *   <li>{@code false} - Validation failed, budget cycle not created</li>
     * </ul>
     *
     * <p><b>Example Validation Scenarios:</b></p>
     * <ul>
     *   <li>Allowance = -100 → Invalid (negative), returns {@code false}</li>
     *   <li>Start Date = 2024-12-31, End Date = 2024-12-01 → Invalid (start after end), returns {@code false}</li>
     *   <li>Allowance = 1000, Start Date = 2024-12-01, End Date = 2024-12-31 → Valid, returns {@code true}</li>
     * </ul>
     *
     * @param allowance the total budget allowance amount (must be ≥ 0)
     * @param startDate the start date of the budget cycle
     * @param endDate the end date of the budget cycle
     * @return {@code true} if validation passes and the budget cycle is created;
     *         {@code false} if validation fails
     */
    public boolean onSetupSumbitted(double allowance, Date startDate, Date endDate);
}