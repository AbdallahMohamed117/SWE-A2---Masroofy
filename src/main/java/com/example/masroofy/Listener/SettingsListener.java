package com.example.masroofy.Listener;

/**
 * Listener interface for handling user interactions on the Settings screen.
 * <p>
 * The {@code SettingsListener} interface defines callback methods for all user
 * actions that can occur on the Settings screen. Implementations (typically
 * the {@code SettingsController}) handle PIN changes, financial cycle resets,
 * and navigation back to the dashboard.
 * </p>
 *
 * <p><b>Available Actions:</b></p>
 * <ul>
 *   <li><b>Change PIN:</b> User updates their authentication PIN</li>
 *   <li><b>Clear Cycle:</b> User resets the current financial budget cycle</li>
 *   <li><b>Back Navigation:</b> User returns to the dashboard</li>
 * </ul>
 *
 * <p><b>PIN Change Validation (implemented by controller):</b></p>
 * <ul>
 *   <li>Current PIN must match the stored PIN</li>
 *   <li>New PIN must be at least 4 digits</li>
 *   <li>Both current and new PIN fields cannot be empty</li>
 * </ul>
 *
 * <p><b>Cycle Clear Behavior:</b></p>
 * Resetting the cycle clears all financial data (transactions, budget settings)
 * and navigates the user back to the Setup screen to configure a new budget cycle.
 *
 * @version 1.0
 * @since 1.0
 * @see com.example.masroofy.Controller.SettingsController
 * @see com.example.masroofy.View.SettingsView
 * @see com.example.masroofy.Model.Pin
 * @see com.example.masroofy.Model.Setup
 */
public interface SettingsListener {

    /**
     * Called when the user clicks the "Change PIN" button.
     * <p>
     * This method is triggered when the user has entered their current PIN
     * and a new PIN, then submits the PIN change request.
     * </p>
     *
     * <p><b>Implementation Responsibilities:</b></p>
     * <ul>
     *   <li>Validate that current PIN is not empty</li>
     *   <li>Validate that new PIN is not empty</li>
     *   <li>Verify that current PIN matches the stored PIN</li>
     *   <li>Ensure new PIN meets minimum length requirements (at least 4 digits)</li>
     *   <li>Update the PIN in the database if validation passes</li>
     *   <li>Display appropriate success or error messages to the user</li>
     * </ul>
     *
     * @param currentPin the user's current PIN for verification
     * @param newPin the new PIN to set
     */
    void onChangePinClicked(String currentPin, String newPin);

    /**
     * Called when the user clicks the "Clear Cycle" button.
     * <p>
     * This method is triggered when the user wants to reset the current
     * financial budget cycle and start over with fresh settings.
     * </p>
     *
     * <p><b>Implementation Responsibilities:</b></p>
     * <ul>
     *   <li>Clear all transaction data from the database</li>
     *   <li>Reset budget settings and allowances</li>
     *   <li>Navigate the user back to the Setup screen</li>
     *   <li>Optionally show a confirmation dialog before clearing</li>
     * </ul>
     *
     * <p><b>Typical Use Cases:</b></p>
     * <ul>
     *   <li>Starting a new month or semester budget</li>
     *   <li>Resetting after significant overspending</li>
     *   <li>Completely fresh start of the application</li>
     * </ul>
     */
    void onClearCycleClicked();

    /**
     * Called when the back button is clicked.
     * <p>
     * This method is triggered when the user wants to navigate back to the
     * Dashboard screen without making any changes.
     * </p>
     *
     * @return {@code true} if navigation back is allowed, {@code false} to cancel
     */
    boolean onBackClicked();
}