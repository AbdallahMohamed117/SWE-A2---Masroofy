package com.example.masroofy.Listener;

/**
 * Listener interface for handling PIN submission events.
 * <p>
 * The {@code PinListener} interface defines a callback method for PIN entry
 * events on the PIN authentication screen. Implementations (typically the
 * {@code PinController}) validate the submitted PIN and respond with success
 * or failure.
 * </p>
 *
 * <p><b>Usage Scenarios:</b></p>
 * <ul>
 *   <li><b>First-time setup:</b> No PIN exists in the system → Save the new PIN</li>
 *   <li><b>Returning user:</b> PIN exists → Validate against stored PIN</li>
 *   <li><b>Failed attempts:</b> Track invalid submissions and implement lockout</li>
 * </ul>
 *
 * <p><b>Typical Implementation Flow:</b></p>
 * <ol>
 *   <li>User enters a PIN in the view</li>
 *   <li>View calls {@link #onPinSubmitted(String)} on the listener</li>
 *   <li>Listener validates the PIN against the model</li>
 *   <li>Listener returns {@code true} for success or {@code false} for failure</li>
 *   <li>View responds accordingly (navigate on success, show error on failure)</li>
 * </ol>
 *
 * @version 1.0
 * @since 1.0
 * @see com.example.masroofy.Controller.PinController
 * @see com.example.masroofy.View.PinView
 */
public interface PinListener {

    /**
     * Called when a PIN is submitted by the user.
     * <p>
     * This method is triggered when the user has entered a PIN and confirmed
     * their entry. The implementation should validate the PIN and return an
     * appropriate response.
     * </p>
     *
     * <p><b>Return Value Interpretation:</b></p>
     * <ul>
     *   <li>{@code true} - PIN is valid (first-time creation or correct match)</li>
     *   <li>{@code false} - PIN is invalid (incorrect PIN)</li>
     * </ul>
     *
     * <p><b>Implementation Responsibilities:</b></p>
     * <ul>
     *   <li>Check if a PIN already exists in the system</li>
     *   <li>If no PIN exists, save the submitted PIN as the new PIN</li>
     *   <li>If PIN exists, verify it matches the stored PIN</li>
     *   <li>Track failed attempts and implement lockout if necessary</li>
     *   <li>Display appropriate error messages for invalid attempts</li>
     * </ul>
     *
     * @param pin the PIN string submitted by the user
     * @return {@code true} if the PIN is valid or successfully created;
     *         {@code false} if the PIN is invalid
     */
    public boolean onPinSubmitted(String pin);
}