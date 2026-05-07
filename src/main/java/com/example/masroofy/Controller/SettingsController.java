package com.example.masroofy.Controller;

import com.example.masroofy.Listener.SettingsListener;
import com.example.masroofy.Model.Pin;
import com.example.masroofy.Model.Setup;
import com.example.masroofy.View.SettingsView;

/**
 * Controller for the Settings screen in the Masroofy application.
 * <p>
 * The {@code SettingsController} manages user preferences and application
 * configuration options. It handles PIN changes and financial cycle resets.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Validating and processing PIN change requests</li>
 *   <li>Clearing the current financial cycle and resetting to setup mode</li>
 *   <li>Providing user feedback for validation errors and success states</li>
 * </ul>
 *
 * <p><b>PIN Validation Rules:</b></p>
 * <ul>
 *   <li>Current PIN cannot be empty or null</li>
 *   <li>New PIN cannot be empty or null</li>
 *   <li>Current PIN must match the stored PIN</li>
 *   <li>New PIN must be at least 4 digits long</li>
 * </ul>
 *
 * <p><b>Cycle Reset Behavior:</b></p>
 * Clearing the cycle resets all financial data and navigates the user back
 * to the Setup screen to configure a new financial cycle.
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractController
 * @see SettingsListener
 * @see Pin
 * @see Setup
 * @see SettingsView
 */
public class SettingsController implements AbstractController, SettingsListener {

    /** The PIN model for authenticating and updating user PIN. */
    private Pin pinModel;

    /** The Setup model for resetting the financial cycle. */
    private Setup setupModel;

    /** The Settings view for displaying UI and user interaction. */
    private SettingsView view;

    /**
     * Constructs a {@code SettingsController} with the specified models and view.
     * <p>
     * Initializes the controller by setting up the model-view references and
     * registering this controller as the settings listener.
     * </p>
     *
     * @param pinModel the PIN model for PIN verification and updates
     * @param setupModel the Setup model for cycle reset operations
     * @param view the Settings view for UI display
     * @throws NullPointerException if any parameter is {@code null}
     */
    public SettingsController(Pin pinModel, Setup setupModel, SettingsView view) {
        if (pinModel == null) {
            throw new NullPointerException("Pin model cannot be null");
        }
        if (setupModel == null) {
            throw new NullPointerException("Setup model cannot be null");
        }
        if (view == null) {
            throw new NullPointerException("Settings view cannot be null");
        }
        this.pinModel = pinModel;
        this.setupModel = setupModel;
        this.view = view;
        view.setSettingsListener(this);
    }

    /**
     * This method is intentionally left empty as the Settings controller does
     * not require an initial view print operation.
     * <p>
     * The Settings view loads its own initial state when displayed.
     * </p>
     */
    @Override
    public void printView() {
        // Settings view loads its own initial state
    }

    /**
     * Handles PIN change requests from the user.
     * <p>
     * This method performs comprehensive validation before updating the PIN:
     * </p>
     *
     * <p><b>Validation Sequence:</b></p>
     * <ol>
     *   <li>Hides any existing PIN error messages</li>
     *   <li>Checks that current PIN is provided (not null or empty)</li>
     *   <li>Checks that new PIN is provided (not null or empty)</li>
     *   <li>Verifies that current PIN matches the stored PIN</li>
     *   <li>Ensures new PIN meets minimum length requirement (4 digits)</li>
     * </ol>
     *
     * <p><b>Success Path:</b></p>
     * <ul>
     *   <li>Updates the PIN in the database</li>
     *   <li>Shows success message and success indicator</li>
     * </ul>
     *
     * <p><b>Failure Paths:</b></p>
     * <ul>
     *   <li>Missing fields → Shows specific error message</li>
     *   <li>Incorrect current PIN → Shows authentication error</li>
     *   <li>PIN too short → Shows length validation error</li>
     *   <li>Database update failure → Shows generic failure message</li>
     * </ul>
     *
     * @param currentPin the user's current PIN for verification
     * @param newPin the new PIN to set
     */
    @Override
    public void onChangePinClicked(String currentPin, String newPin) {
        view.hidePinError();

        // Validate current PIN is provided
        if (currentPin == null || currentPin.trim().isEmpty()) {
            view.showPinError("Current PIN is required.");
            return;
        }

        // Validate new PIN is provided
        if (newPin == null || newPin.trim().isEmpty()) {
            view.showPinError("New PIN is required.");
            return;
        }

        // Authenticate current PIN
        if (!pinModel.checkPin(currentPin)) {
            view.showPinError("Current PIN is incorrect.");
            return;
        }

        // Validate new PIN length
        if (newPin.length() < 4) {
            view.showPinError("New PIN must be at least 4 digits.");
            return;
        }

        // Attempt to update the PIN
        boolean success = pinModel.updatePin(currentPin, newPin);
        if (success) {
            view.showPinSuccess();
            view.showSuccessMessage("PIN changed successfully!");
        } else {
            view.showPinError("Failed to update PIN. Please try again.");
        }
    }

    /**
     * Handles clearing the current financial cycle.
     * <p>
     * This method resets all financial data by calling the setup model's
     * {@code clearCycle()} method, then navigates the user back to the
     * Setup screen to configure a new financial cycle.
     * </p>
     * <p>
     * This is typically used when a user wants to start a new budget period
     * (e.g., new month, new semester, or resetting after overspending).
     * </p>
     */
    @Override
    public void onClearCycleClicked() {
        setupModel.clearCycle();
        view.navigateToSetup();
    }

    /**
     * Handles back button navigation events.
     * <p>
     * This method always returns {@code true} to indicate that navigation back
     * to the previous screen (Dashboard) is permitted.
     * </p>
     *
     * @return {@code true} always, indicating back navigation is allowed
     */
    @Override
    public boolean onBackClicked() {
        return true;
    }
}