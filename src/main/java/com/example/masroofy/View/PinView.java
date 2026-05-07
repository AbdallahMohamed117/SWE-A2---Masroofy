package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.example.masroofy.Listener.PinListener;

/**
 * View class for the PIN entry screen in the Masroofy application.
 * <p>
 * The {@code PinView} manages the PIN entry UI, displaying visual feedback
 * for digit entry, error states, and lockout conditions. It uses four circular
 * indicators that change color as the user enters digits.
 * </p>
 *
 * <p><b>UI Components:</b></p>
 * <ul>
 *   <li>Four circular dots representing PIN digits (1-4)</li>
 *   <li>Numeric keypad buttons (0-9)</li>
 *   <li>Delete button for removing the last digit</li>
 *   <li>Submit button for PIN validation</li>
 * </ul>
 *
 * <p><b>Color States:</b></p>
 * <ul>
 *   <li><b>FILLED (#38bdf8):</b> Digit has been entered (blue)</li>
 *   <li><b>EMPTY (#1e293b):</b> No digit entered yet (dark gray)</li>
 *   <li><b>ERROR (#fb7185):</b> Invalid PIN entered (red/pink)</li>
 *   <li><b>SUCCESS (#10B981):</b> PIN accepted (green)</li>
 * </ul>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Visual feedback for each entered digit</li>
 *   <li>Error state highlighting for invalid PINs</li>
 *   <li>Lockout mechanism with countdown timer</li>
 *   <li>Automatic reset after error</li>
 *   <li>Maximum 4-digit PIN input</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractView
 * @see PinListener
 * @see com.example.masroofy.Controller.PinController
 */
public class PinView implements AbstractView {

    /** Color for filled PIN digits (blue). */
    private static final Color FILLED = Color.web("#38bdf8");

    /** Color for empty PIN digit positions (dark gray). */
    private static final Color EMPTY  = Color.web("#1e293b");

    /** Color for error state (red/pink). */
    private static final Color ERROR  = Color.web("#fb7185");

    private PinListener pinListener;
    private Runnable onNavigateToDashboard;

    // FXML Injected Components
    @FXML private Circle pinDot1;
    @FXML private Circle pinDot2;
    @FXML private Circle pinDot3;
    @FXML private Circle pinDot4;
    @FXML private Button btnSubmitPin;

    /** Array of circle references for easy iteration. */
    private final Circle[] dots = new Circle[4];

    /** StringBuilder to accumulate the entered PIN digits. */
    private final StringBuilder enteredPin = new StringBuilder();

    /**
     * Sets the PinListener for handling PIN submission events.
     *
     * @param pl the PinListener to set
     */
    public void setPinListener(PinListener pl) {
        pinListener = pl;
    }

    /**
     * Sets the callback for navigating to the Dashboard screen.
     *
     * @param r the Runnable to execute when PIN is successfully validated
     */
    public void setOnNavigateToDashboard(Runnable r) {
        this.onNavigateToDashboard = r;
    }

    /**
     * Initializes the view after FXML loading.
     * <p>
     * Populates the dots array with the injected Circle components for easy access.
     * </p>
     */
    @FXML
    public void initialize() {
        dots[0] = pinDot1;
        dots[1] = pinDot2;
        dots[2] = pinDot3;
        dots[3] = pinDot4;
    }

    /**
     * Displays the PIN entry screen.
     * <p>
     * Calls {@link #showPinEntry()} to reset the entry state.
     * </p>
     */
    @Override
    public void printScreen() {
        showPinEntry();
    }

    /**
     * Returns the currently entered PIN string.
     *
     * @return the entered PIN as a String
     */
    public String getEnteredPin() {
        return enteredPin.toString();
    }

    /**
     * Updates the color of a specific dot at the given index.
     *
     * @param index the position of the dot (0-3)
     * @param color the color to set
     */
    private void updateDot(int index, Color color) {
        dots[index].setFill(color);
    }

    /**
     * Updates all dots to the specified color.
     *
     * @param color the color to set for all dots
     */
    private void updateDots(Color color) {
        for (Circle dot : dots) {
            dot.setFill(color);
        }
    }

    /**
     * Resets the PIN entry state for a new entry attempt.
     * <p>
     * Clears the entered PIN buffer and sets all dots to EMPTY color.
     * </p>
     */
    public void showPinEntry() {
        enteredPin.setLength(0);
        updateDots(EMPTY);
    }

    /**
     * Displays an error message for invalid PIN entry.
     * <p>
     * Changes all dots to ERROR color for one second, then automatically
     * resets the entry state back to empty.
     * </p>
     *
     * @param msg the error message to display (currently not used for display)
     */
    public void showErrorMessage(String msg) {
        updateDots(ERROR);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                javafx.application.Platform.runLater(() -> {
                    enteredPin.setLength(0);
                    updateDots(EMPTY);
                });
            } catch (InterruptedException ignored) {}
        }).start();
    }

    /**
     * Displays a lockout state after too many failed attempts.
     * <p>
     * Disables the submit button and shows a countdown timer. After the specified
     * number of seconds, the button is re-enabled and the PIN entry is reset.
     * </p>
     *
     * @param seconds the number of seconds to lock out the user
     */
    public void showLockout(int seconds) {
        btnSubmitPin.setDisable(true);
        btnSubmitPin.setText("Try again in " + seconds + "s");
        new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000L);
                javafx.application.Platform.runLater(() -> {
                    btnSubmitPin.setDisable(false);
                    btnSubmitPin.setText("UNLOCK NOW");
                    showPinEntry();
                });
            } catch (InterruptedException ignored) {}
        }).start();
    }

    /**
     * Event handler for digit button clicks.
     * <p>
     * Appends the clicked digit to the entered PIN buffer and updates the
     * corresponding dot to FILLED color. Maximum input is 4 digits.
     * </p>
     *
     * @param e the ActionEvent from the digit button
     */
    @FXML
    private void onDigitClicked(javafx.event.ActionEvent e) {
        if (enteredPin.length() >= 4) return;
        enteredPin.append(((Button) e.getSource()).getText());
        updateDot(enteredPin.length() - 1, FILLED);
    }

    /**
     * Event handler for the delete button click.
     * <p>
     * Removes the last digit from the entered PIN buffer and sets the
     * corresponding dot back to EMPTY color.
     * </p>
     */
    @FXML
    private void onDeleteClicked() {
        if (enteredPin.length() == 0) return;
        enteredPin.deleteCharAt(enteredPin.length() - 1);
        updateDot(enteredPin.length(), EMPTY);
    }

    /**
     * Event handler for the submit button click.
     * <p>
     * Submits the entered PIN to the listener for validation. If the PIN is
     * valid, all dots turn green and the user is navigated to the Dashboard.
     * </p>
     */
    @FXML
    private void onSubmitPin() {
        if (pinListener.onPinSubmitted(getEnteredPin())) {
            updateDots(Color.web("#10B981"));
            if (onNavigateToDashboard != null) onNavigateToDashboard.run();
        }
    }
}