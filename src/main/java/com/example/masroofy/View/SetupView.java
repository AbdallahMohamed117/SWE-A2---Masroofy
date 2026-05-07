package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.masroofy.Listener.SetupListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * View class for the Setup screen in the Masroofy application.
 * <p>
 * The {@code SetupView} manages the first-time configuration UI, allowing users
 * to set up their budget cycle by entering an allowance amount and selecting
 * start and end dates for the budget period.
 * </p>
 *
 * <p><b>UI Components:</b></p>
 * <ul>
 *   <li>Allowance amount TextField for entering the total budget</li>
 *   <li>Start Date DatePicker for selecting the budget cycle start date</li>
 *   <li>End Date DatePicker for selecting the budget cycle end date</li>
 *   <li>Start Cycle button to submit the setup data</li>
 *   <li>Error message Label for validation feedback</li>
 * </ul>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Date selection using JavaFX DatePicker with system default timezone</li>
 *   <li>Error message display for invalid inputs</li>
 *   <li>Automatic navigation to Dashboard on successful setup</li>
 * </ul>
 *
 * <p><b>Validation (handled by controller):</b></p>
 * <ul>
 *   <li>Allowance must be non-negative</li>
 *   <li>Start date must not be after end date</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractView
 * @see SetupListener
 * @see com.example.masroofy.Controller.SetupController
 */
public class SetupView implements AbstractView {

    private SetupListener eventListener;
    private Runnable onNavigateToDashboard;

    // FXML Injected Components
    @FXML private TextField etAllowanceAmount;
    @FXML private DatePicker etStartDate;
    @FXML private DatePicker etEndDate;
    @FXML private Label tvSetupError;
    @FXML private Button btnStartCycle;

    /**
     * Sets the SetupListener for handling setup submission events.
     *
     * @param sl the SetupListener to set
     */
    public void setEventListener(SetupListener sl) {
        eventListener = sl;
    }

    /**
     * Returns the allowance amount entered in the text field.
     *
     * @return the allowance amount as a String
     */
    public String getAmountText() {
        return etAllowanceAmount.getText();
    }

    /**
     * Returns the selected start date from the DatePicker.
     * <p>
     * Converts the LocalDate to a Date using the system's default time zone.
     * </p>
     *
     * @return the start date as a {@link Date} object
     * @throws NullPointerException if no start date is selected
     */
    public Date getStartDate() {
        LocalDate lc = etStartDate.getValue();
        Date date = Date.from(lc.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * Returns the selected end date from the DatePicker.
     * <p>
     * Converts the LocalDate to a Date using the system's default time zone.
     * </p>
     *
     * @return the end date as a {@link Date} object
     * @throws NullPointerException if no end date is selected
     */
    public Date getEndDate() {
        LocalDate lc = etEndDate.getValue();
        Date date = Date.from(lc.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * Sets the callback for navigating to the Dashboard screen after successful setup.
     *
     * @param r the Runnable to execute when navigation to Dashboard is triggered
     */
    public void setOnNavigateToDashboard(Runnable r) {
        this.onNavigateToDashboard = r;
    }

    /**
     * Resets the view state by hiding any error messages.
     */
    @Override
    public void printScreen() {
        tvSetupError.setVisible(false);
    }

    /**
     * This method is currently empty and reserved for future implementation.
     * <p>
     * Intended to display the setup screen with any necessary initialization.
     * </p>
     */
    public void showSetupScreen() {
        // Reserved for future implementation
    }

    /**
     * Displays an error message for validation failures.
     * <p>
     * Sets the error message text and makes the error label visible.
     * </p>
     *
     * @param msg the error message to display
     */
    public void showErrorMessage(String msg) {
        tvSetupError.setText(msg);
        tvSetupError.setVisible(true);
    }

    /**
     * Event handler for the Start Cycle button click.
     * <p>
     * Retrieves the allowance amount and selected dates, then notifies the
     * listener to validate and save the budget cycle. If the setup is successful,
     * navigates to the Dashboard screen.
     * </p>
     *
     * <p><b>Note:</b></p>
     * This method assumes that the allowance amount is a valid double.
     * No parsing error handling is currently implemented.
     */
    @FXML
    public void onStartCycleClicked() {
        double all = Double.parseDouble(getAmountText());
        Date start = getStartDate();
        Date end = getEndDate();

        if (eventListener.onSetupSumbitted(all, start, end)) {
            if (onNavigateToDashboard != null) onNavigateToDashboard.run();
        }
    }
}