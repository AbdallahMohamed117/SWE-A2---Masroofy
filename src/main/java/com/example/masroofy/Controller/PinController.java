package com.example.masroofy.Controller;

import com.example.masroofy.Model.*;
import com.example.masroofy.View.*;
import com.example.masroofy.Listener.PinListener;

/**
 * Controller for the PIN authentication screen in the Masroofy application.
 * <p>
 * The {@code PinController} manages PIN verification and setup functionality.
 * It handles both first-time PIN creation and subsequent PIN validation for
 * returning users. The controller implements a retry limit mechanism to prevent
 * brute-force attacks.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Creating a new PIN for first-time users</li>
 *   <li>Validating PIN for returning users</li>
 *   <li>Tracking failed attempt counts</li>
 *   <li>Locking out the user after 5 failed attempts</li>
 *   <li>Displaying appropriate error messages for invalid PINs</li>
 * </ul>
 *
 * <p><b>Authentication Flow:</b></p>
 * <ol>
 *   <li>If no PIN exists in the system → Create and save the new PIN</li>
 *   <li>If PIN exists and submitted PIN matches → Successful authentication</li>
 *   <li>If PIN exists and submitted PIN does not match → Failed attempt, increment counter</li>
 *   <li>After 5 failed attempts → Show lockout message and disable input for 300 seconds</li>
 * </ol>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractController
 * @see PinListener
 * @see Pin
 * @see PinView
 */
public class PinController implements AbstractController, PinListener {

    /** The PIN model handling PIN storage and verification logic. */
    private Pin model;

    /** The PIN view for displaying the PIN entry UI and messages. */
    private PinView view;

    /** The number of failed PIN entry attempts by the user. */
    private int tries;

    /**
     * Constructs a {@code PinController} with the specified model and view.
     * <p>
     * Initializes the controller by setting up the model-view references,
     * registering this controller as the PIN listener, and resetting the
     * failed attempt counter to zero.
     * </p>
     *
     * @param m the PIN model containing PIN storage and verification logic
     * @param v the PIN view for UI display and user interaction
     * @throws NullPointerException if {@code m} or {@code v} is {@code null}
     */
    public PinController(Pin m, PinView v) {
        if (m == null) {
            throw new NullPointerException("Pin model cannot be null");
        }
        if (v == null) {
            throw new NullPointerException("Pin view cannot be null");
        }
        model = m;
        view = v;
        view.setPinListener(this);
        tries = 0;
    }

    /**
     * This method is intentionally left empty as PIN controller does not
     * require an initial view print operation.
     * <p>
     * The PIN view is already displayed when the controller is instantiated.
     * </p>
     */
    @Override
    public void printView() {
        // PIN view is displayed automatically; no refresh needed initially
    }

    /**
     * Handles PIN submission from the user.
     * <p>
     * This method implements the complete PIN authentication logic:
     * </p>
     *
     * <p><b>Case 1: No PIN exists (First-time setup)</b></p>
     * <ul>
     *   <li>Saves the submitted PIN to the model</li>
     *   <li>Returns {@code true} to indicate success</li>
     * </ul>
     *
     * <p><b>Case 2: PIN exists and submitted PIN matches</b></p>
     * <ul>
     *   <li>Sets the PIN in the model (refreshes authentication state)</li>
     *   <li>Returns {@code true} to indicate success</li>
     * </ul>
     *
     * <p><b>Case 3: PIN exists but submitted PIN does not match</b></p>
     * <ul>
     *   <li>Increments the failed attempt counter</li>
     *   <li>If attempts exceed 5: Shows lockout message for 300 seconds</li>
     *   <li>If attempts ≤ 5: Shows generic error message</li>
     *   <li>Returns {@code false} to indicate failure</li>
     * </ul>
     *
     * @param pin the PIN submitted by the user
     * @return {@code true} if PIN validation succeeds or PIN is created;
     *         {@code false} if the submitted PIN is invalid
     */
    @Override
    public boolean onPinSubmitted(String pin) {
        boolean validate = true;

        // First-time setup: No PIN exists in the system
        if (!model.pinExist()) {
            model.setPin(pin);
        }
        // PIN exists but submitted PIN is incorrect
        else if (!model.checkPin(pin)) {
            validate = false;
            ++tries;

            // Lockout after 5 failed attempts
            if (tries > 5) {
                view.showLockout(300);
            } else {
                view.showErrorMessage("Please Enter The Correct Pin");
            }
        }
        // PIN exists and submitted PIN is correct
        else if (model.checkPin(pin)) {
            model.setPin(pin);
        }

        return validate;
    }
}