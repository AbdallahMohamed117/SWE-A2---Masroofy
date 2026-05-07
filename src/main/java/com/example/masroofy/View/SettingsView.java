package com.example.masroofy.View;

import com.example.masroofy.Listener.SettingsListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * View class for the Settings screen in the Masroofy application.
 * <p>
 * The {@code SettingsView} manages the settings UI, providing controls for
 * PIN changes and clearing the financial cycle. It includes validation
 * feedback displays and confirmation dialogs for destructive actions.
 * </p>
 *
 * <p><b>UI Components:</b></p>
 * <ul>
 *   <li>Current PIN input TextField</li>
 *   <li>New PIN input TextField</li>
 *   <li>Change PIN button</li>
 *   <li>Clear Cycle button (with confirmation dialog)</li>
 *   <li>Error/Success message label for PIN operations</li>
 * </ul>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>PIN change with validation feedback (error/success messages)</li>
 *   <li>Clear cycle with confirmation dialog to prevent accidental data loss</li>
 *   <li>Dynamic message styling (green for success, red for errors)</li>
 *   <li>Automatic field clearing on successful PIN change</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractView
 * @see SettingsListener
 * @see com.example.masroofy.Controller.SettingsController
 */
public class SettingsView implements AbstractView {

    // FXML Injected Components
    @FXML private TextField etCurrentPin;
    @FXML private TextField etNewPin;
    @FXML private Label tvPinError;
    @FXML private Label tvClearError;
    @FXML private Button btnChangePin;
    @FXML private Button btnClearCycle;

    private SettingsListener listener;
    private Runnable onNavigateBack;
    private Runnable onNavigateToSetup;

    /**
     * Initializes the view after FXML loading.
     * <p>
     * Hides the PIN error and clear error message labels initially.
     * </p>
     */
    @FXML
    public void initialize() {
        tvPinError.setVisible(false);
        tvClearError.setVisible(false);
    }

    /**
     * This method is intentionally empty as the Settings view does not require
     * a full screen refresh. UI updates are handled by specific methods.
     */
    @Override
    public void printScreen() {
        // Settings view initializes its own state via FXML
    }

    /**
     * Sets the SettingsListener for handling user interactions.
     *
     * @param listener the SettingsListener to set
     */
    public void setSettingsListener(SettingsListener listener) {
        this.listener = listener;
    }

    /**
     * Sets the callback for navigating back to the Dashboard screen.
     *
     * @param r the Runnable to execute when back navigation is triggered
     */
    public void setOnNavigateBack(Runnable r) {
        this.onNavigateBack = r;
    }

    /**
     * Sets the callback for navigating to the Setup screen (after clearing cycle).
     *
     * @param r the Runnable to execute when navigating to Setup
     */
    public void setOnNavigateToSetup(Runnable r) {
        this.onNavigateToSetup = r;
    }

    /**
     * Returns the current PIN entered in the Current PIN field.
     *
     * @return the current PIN as a String
     */
    public String getCurrentPin() {
        return etCurrentPin.getText();
    }

    /**
     * Returns the new PIN entered in the New PIN field.
     *
     * @return the new PIN as a String
     */
    public String getNewPin() {
        return etNewPin.getText();
    }

    /**
     * Displays an error message for PIN-related operations.
     * <p>
     * Sets the message text to red and makes the label visible.
     * </p>
     *
     * @param msg the error message to display
     */
    public void showPinError(String msg) {
        tvPinError.setText(msg);
        tvPinError.setStyle("-fx-text-fill: #fb7185;");
        tvPinError.setVisible(true);
    }

    /**
     * Hides the PIN error/success message label.
     */
    public void hidePinError() {
        tvPinError.setVisible(false);
    }

    /**
     * Displays success state after a successful PIN change.
     * <p>
     * Clears both PIN input fields and hides any error messages.
     * </p>
     */
    public void showPinSuccess() {
        etCurrentPin.clear();
        etNewPin.clear();
        tvPinError.setVisible(false);
    }

    /**
     * Displays a success message for PIN-related operations.
     * <p>
     * Sets the message text to green and makes the label visible.
     * </p>
     *
     * @param msg the success message to display
     */
    public void showSuccessMessage(String msg) {
        tvPinError.setText(msg);
        tvPinError.setStyle("-fx-text-fill: #10b981;");
        tvPinError.setVisible(true);
    }

    /**
     * Displays an error message for clear cycle operations.
     *
     * @param msg the error message to display
     */
    public void showClearError(String msg) {
        tvClearError.setText(msg);
        tvClearError.setVisible(true);
    }

    /**
     * Navigates to the Setup screen (called after clearing the cycle).
     */
    public void navigateToSetup() {
        if (onNavigateToSetup != null) onNavigateToSetup.run();
    }

    /**
     * Event handler for the Change PIN button click.
     * <p>
     * Notifies the listener to process the PIN change request with the
     * current PIN and new PIN values.
     * </p>
     */
    @FXML
    public void onChangePinClicked() {
        if (listener != null) {
            listener.onChangePinClicked(getCurrentPin(), getNewPin());
        }
    }

    /**
     * Event handler for the Clear Cycle button click.
     * <p>
     * Shows a confirmation dialog before proceeding to prevent accidental
     * data loss. If the user confirms, notifies the listener to clear the cycle.
     * </p>
     *
     * <p><b>Warning Message:</b> "This will permanently delete all your data and start fresh."</p>
     */
    @FXML
    public void onClearCycleClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Cycle");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("This will permanently delete all your data and start fresh.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (listener != null) {
                    listener.onClearCycleClicked();
                }
            }
        });
    }

    /**
     * Event handler for the back button click.
     * <p>
     * Checks with the listener if back navigation is allowed before proceeding.
     * </p>
     */
    @FXML
    public void onBackClicked() {
        if (listener != null && listener.onBackClicked()) {
            if (onNavigateBack != null) onNavigateBack.run();
        }
    }
}