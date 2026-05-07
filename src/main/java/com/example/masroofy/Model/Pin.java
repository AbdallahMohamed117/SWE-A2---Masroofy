package com.example.masroofy.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Model class for managing PIN authentication and user state.
 * <p>
 * The {@code Pin} class handles all PIN-related database operations including
 * PIN creation, verification, existence checking, updating, and user state
 * management. It supports both first-time PIN setup and subsequent authentication.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Creating a new PIN for first-time users</li>
 *   <li>Verifying entered PIN against stored PIN</li>
 *   <li>Checking if a PIN already exists in the system</li>
 *   <li>Updating an existing PIN after successful verification</li>
 *   <li>Determining if the user is in an active state</li>
 * </ul>
 *
 * <p><b>Database Table Used:</b></p>
 * <ul>
 *   <li>{@code Student} - Stores student_pincode, student_state, and budget_id</li>
 * </ul>
 *
 * <p><b>Note:</b></p>
 * The current implementation assumes a single user (single row in the Student table).
 * The application is designed for single-user use on a personal device.
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractModel
 * @see com.example.masroofy.Controller.PinController
 * @see com.example.masroofy.Model.Entity.State
 */
public class Pin extends AbstractModel {

    /**
     * Constructs a Pin model instance.
     * <p>
     * Initializes the database connection through the parent {@link AbstractModel}
     * constructor.
     * </p>
     */
    public Pin() {
        super();
    }

    /**
     * Creates and saves a new PIN for first-time setup.
     * <p>
     * This method inserts a new record into the Student table with the provided
     * PIN, sets the user state to 'ACTIVE', and associates the default budget
     * (budget_id = 1).
     * </p>
     *
     * <p><b>Important:</b></p>
     * This method assumes a single-user application and will insert a new row
     * without checking for existing data. It should only be called during
     * first-time setup when no PIN exists.
     *
     * @param pin the PIN to store (typically 4 or more digits)
     */
    public void setPin(String pin) {
        String setPinQuery = "INSERT INTO Student (student_pincode, student_state, budget_id) VALUES (?, 'ACTIVE', ?)";

        try (PreparedStatement setPinStatement = connection.prepareStatement(setPinQuery)){
            setPinStatement.setString(1, pin);
            setPinStatement.setInt(2, 1);

            setPinStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Verifies if the entered PIN matches the stored PIN.
     * <p>
     * This method retrieves the stored PIN from the database and compares it
     * with the entered PIN using {@link Objects#equals(Object, Object)}.
     * </p>
     *
     * <p><b>Security Note:</b></p>
     * Future versions should consider storing PINs using a secure hashing
     * algorithm instead of plain text.
     *
     * @param enteredPin the PIN entered by the user
     * @return {@code true} if the entered PIN matches the stored PIN,
     *         {@code false} otherwise or if no PIN exists
     */
    public boolean checkPin(String enteredPin) {
        String checkPinQuery = "SELECT student_pincode FROM Student";

        try (PreparedStatement checkPinStatement = connection.prepareStatement(checkPinQuery)) {

            ResultSet result = checkPinStatement.executeQuery();

            if (result.next()){
                String storedPin = result.getString("student_pincode");
                return Objects.equals(storedPin, enteredPin);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks whether a PIN already exists in the system.
     * <p>
     * This method determines if the application has been set up by checking
     * for the presence of a Student record.
     * </p>
     *
     * <p><b>Usage:</b></p>
     * <ul>
     *   <li>During splash screen to decide between PIN and Setup screens</li>
     *   <li>In PinController to determine if this is first-time setup or authentication</li>
     * </ul>
     *
     * @return {@code true} if a PIN exists (Student table has at least one row),
     *         {@code false} otherwise
     */
    public boolean pinExist() {
        String checkPin = "SELECT student_pincode FROM Student";

        try(PreparedStatement checkPinStatement = connection.prepareStatement(checkPin)) {
            ResultSet result = checkPinStatement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Updates the user's PIN after verifying the current PIN.
     * <p>
     * This method first verifies that the provided current PIN is correct,
     * then updates the stored PIN to the new value.
     * </p>
     *
     * <p><b>Update Logic:</b></p>
     * <ol>
     *   <li>Call {@link #checkPin(String)} to verify current PIN</li>
     *   <li>If verification fails, return {@code false}</li>
     *   <li>If verification succeeds, update the PIN in the database</li>
     * </ol>
     *
     * @param currentPin the user's current PIN (for verification)
     * @param newPin the new PIN to set
     * @return {@code true} if the PIN was updated successfully,
     *         {@code false} if the current PIN is incorrect or a database error occurs
     */
    public boolean updatePin(String currentPin, String newPin) {
        if (!checkPin(currentPin)) return false;

        String updatePinQuery = "UPDATE Student SET student_pincode = ? WHERE student_pincode = ?";

        try (PreparedStatement updateStmt = connection.prepareStatement(updatePinQuery)) {
            updateStmt.setString(1, newPin);
            updateStmt.setString(2, currentPin);
            updateStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Determines whether the user is in an active state.
     * <p>
     * This method checks the student_state field in the Student table to see
     * if the user has completed setup and is fully active.
     * </p>
     *
     * <p><b>State Values:</b></p>
     * <ul>
     *   <li>'ACTIVE' - User has completed setup and can access all features</li>
     *   <li>'INACTIVE' - User has not completed setup (should be redirected to Setup screen)</li>
     * </ul>
     *
     * @return {@code true} if the user state is 'ACTIVE', {@code false} if
     *         'INACTIVE' or no user record exists
     */
    public boolean isUserActive() {
        String checkState = "SELECT student_state FROM Student LIMIT 1";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkState)) {
            ResultSet result = checkStmt.executeQuery();
            if (result.next()) {
                return "ACTIVE".equals(result.getString("student_state"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}