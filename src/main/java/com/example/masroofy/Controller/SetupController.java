package com.example.masroofy.Controller;

import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.Budget;
import com.example.masroofy.View.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.example.masroofy.Listener.SetupListener;

/**
 * Controller for the Setup screen in the Masroofy application.
 * <p>
 * The {@code SetupController} handles first-time application configuration
 * and financial cycle setup. It validates user input and creates a new
 * budget cycle with allowance, start date, and end date.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Validating allowance amount (must be non-negative)</li>
 *   <li>Validating date range (start date must be before or equal to end date)</li>
 *   <li>Creating a new budget cycle with validated data</li>
 *   <li>Calculating the daily safe spending limit</li>
 *   <li>Displaying validation error messages</li>
 * </ul>
 *
 * <p><b>Validation Rules:</b></p>
 * <ul>
 *   <li>Allowance cannot be negative</li>
 *   <li>Start date cannot be after end date</li>
 * </ul>
 *
 * <p><b>After Successful Setup:</b></p>
 * The controller creates a {@link Budget} object, sets the allowance and date range,
 * calculates the daily safe limit, and saves the cycle to the model.
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractController
 * @see SetupListener
 * @see Setup
 * @see SetupView
 * @see Budget
 */
public class SetupController implements AbstractController, SetupListener {

    /** The Setup model for storing budget cycle data. */
    private Setup model;

    /** The Setup view for displaying UI and user interaction. */
    private SetupView view;

    /**
     * Constructs a {@code SetupController} with the specified model and view.
     * <p>
     * Initializes the controller by setting up the model-view references and
     * registering this controller as the setup event listener.
     * </p>
     *
     * @param m the Setup model for storing budget configuration
     * @param v the Setup view for UI display and user input
     * @throws NullPointerException if {@code m} or {@code v} is {@code null}
     */
    public SetupController(Setup m, SetupView v) {
        if (m == null) {
            throw new NullPointerException("Setup model cannot be null");
        }
        if (v == null) {
            throw new NullPointerException("Setup view cannot be null");
        }
        model = m;
        view = v;
        view.setEventListener(this);
    }

    /**
     * Displays the setup screen.
     * <p>
     * This method implements the {@link AbstractController} interface by
     * delegating to the view's {@code printScreen()} method to render the
     * setup UI.
     * </p>
     */
    @Override
    public void printView() {
        view.printScreen();
    }

    /**
     * Handles the submission of setup data from the user.
     * <p>
     * This method validates the user input and creates a new budget cycle
     * if all validation checks pass.
     * </p>
     *
     * <p><b>Validation Steps:</b></p>
     * <ol>
     *   <li>Checks that allowance is not negative</li>
     *   <li>Checks that start date is not after end date</li>
     * </ol>
     *
     * <p><b>On Successful Validation:</b></p>
     * <ol>
     *   <li>Creates a new {@link Budget} object</li>
     *   <li>Sets the allowance amount</li>
     *   <li>Sets the start and end dates</li>
     *   <li>Calculates the daily safe spending limit using {@link Budget#setDailysafeLimit()}</li>
     *   <li>Saves the budget cycle to the model via {@link Setup#setCycle(Budget)}</li>
     * </ol>
     *
     * @param allowance the total allowance amount for the budget cycle
     * @param startDate the start date of the budget cycle
     * @param endDate the end date of the budget cycle
     * @return {@code true} if validation passes and the budget cycle is created;
     *         {@code false} if validation fails
     *
     * @see Budget#setAllowance(double)
     * @see Budget#setStartDate(Date)
     * @see Budget#setEndDate(Date)
     * @see Budget#setDailysafeLimit()
     * @see Setup#setCycle(Budget)
     */
    @Override
    public boolean onSetupSumbitted(double allowance, Date startDate, Date endDate) {
        boolean pass = true;

        // Validate allowance amount
        if (allowance < 0) {
            view.showErrorMessage("Invalid Allowance Amount");
            pass = false;
        }

        // Validate date range
        if (startDate.after(endDate)) {
            view.showErrorMessage("Start Date Can't Be Less Than End Date");
            pass = false;
        }

        // Create budget cycle if validation passed
        if (pass) {
            Budget b = new Budget();
            b.setAllowance(allowance);
            b.setStartDate(startDate);
            b.setEndDate(endDate);
            b.setDailysafeLimit();

            model.setCycle(b);
        }

        return pass;
    }
}