package com.example.masroofy.Model;

import com.example.masroofy.Model.Entity.Budget;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Model class for managing budget setup and cycle reset operations.
 * <p>
 * The {@code Setup} class handles the initial configuration of a budget cycle
 * and the complete reset of all financial data. It is used during first-time
 * setup and when a user chooses to clear the current budget cycle.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Creating a new budget cycle with allowance, date range, and daily limits</li>
 *   <li>Setting the student state to ACTIVE after successful setup</li>
 *   <li>Clearing all financial data (transactions, categories, budget) for a fresh start</li>
 *   <li>Resetting student state to INACTIVE when clearing the cycle</li>
 * </ul>
 *
 * <p><b>Database Tables Used:</b></p>
 * <ul>
 *   <li>{@code Budget} - Stores budget configuration (allowance, dates, daily limits)</li>
 *   <li>{@code Student} - Stores student state (ACTIVE/INACTIVE)</li>
 *   <li>{@code Transactions} - Stores transaction records (cleared on reset)</li>
 *   <li>{@code Category} - Stores expense categories (cleared on reset)</li>
 * </ul>
 *
 * <p><b>Setup Flow:</b></p>
 * <ol>
 *   <li>User enters allowance and date range in Setup screen</li>
 *   <li>Controller validates input and creates a {@link Budget} object</li>
 *   <li>This class saves the budget to the database</li>
 *   <li>Student state is updated to 'ACTIVE'</li>
 *   <li>User proceeds to PIN setup and then Dashboard</li>
 * </ol>
 *
 * <p><b>Clear Cycle Flow:</b></p>
 * <ol>
 *   <li>User clicks "Clear Cycle" in Settings screen</li>
 *   <li>This method deletes all transactions, categories, and budget data</li>
 *   <li>Student state is reset to 'INACTIVE'</li>
 *   <li>User is redirected to Setup screen to create a new budget</li>
 * </ol>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractModel
 * @see Budget
 * @see com.example.masroofy.Controller.SetupController
 * @see com.example.masroofy.Controller.SettingsController
 */
public class Setup extends AbstractModel {

    /**
     * Constructs a Setup model instance.
     * <p>
     * Initializes the database connection through the parent {@link AbstractModel}
     * constructor.
     * </p>
     */
    public Setup() {
        super();
    }

    /**
     * Creates and saves a new budget cycle to the database.
     * <p>
     * This method inserts a new budget record into the Budget table and updates
     * the student state to 'ACTIVE'. The budget includes allowance, date range,
     * daily safe limit, original daily limit, and last recalculation date.
     * </p>
     *
     * <p><b>Inserted Fields:</b></p>
     * <ul>
     *   <li>allowance - Total budget amount for the cycle</li>
     *   <li>start_date - Beginning date of the budget period</li>
     *   <li>end_date - Ending date of the budget period</li>
     *   <li>daily_safe_limit - Calculated per-day spending limit</li>
     *   <li>original_daily_limit - Initial daily limit (same as daily_safe_limit)</li>
     *   <li>last_recalc_date - Current date when the budget was created</li>
     * </ul>
     *
     * <p><b>Note:</b></p>
     * This method assumes only one budget exists at a time. For a fresh start,
     * call {@link #clearCycle()} before calling this method.
     *
     * @param budget the Budget object containing allowance, date range, and daily limits
     */
    public void setCycle(Budget budget) {
        String createBudgetQuery = "INSERT INTO Budget (allowance, start_date, end_date, daily_safe_limit, original_daily_limit, last_recalc_date) VALUES (?, ?, ?, ?, ?, ?)";
        String updateStudentQuery = "UPDATE Student SET student_state = 'ACTIVE'";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try (PreparedStatement createBudgetStatement = connection.prepareStatement(createBudgetQuery);
             PreparedStatement updateStudentStateStatement = connection.prepareStatement(updateStudentQuery)) {

            createBudgetStatement.setDouble(1, budget.getAllowance());
            createBudgetStatement.setString(2, sdf.format(budget.getStartDate()));
            createBudgetStatement.setString(3, sdf.format(budget.getEndDate()));
            createBudgetStatement.setDouble(4, budget.getDailySafeLimit());
            createBudgetStatement.setDouble(5, budget.getDailySafeLimit());
            createBudgetStatement.setString(6, sdf.format(new java.util.Date()));

            createBudgetStatement.executeUpdate();
            updateStudentStateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears all financial data for a fresh start.
     * <p>
     * This method performs a complete reset of the application's financial data,
     * deleting all transactions, categories, and the current budget. It also
     * sets the student state back to 'INACTIVE', requiring the user to go through
     * the setup process again.
     * </p>
     *
     * <p><b>Deleted Data:</b></p>
     * <ul>
     *   <li>All transaction records from the Transactions table</li>
     *   <li>All category records from the Category table</li>
     *   <li>The current budget from the Budget table</li>
     * </ul>
     *
     * <p><b>Updated Data:</b></p>
     * <ul>
     *   <li>Student state changed from 'ACTIVE' to 'INACTIVE'</li>
     * </ul>
     *
     * <p><b>Transaction Safety:</b></p>
     * Uses database transactions with auto-commit disabled to ensure all
     * operations succeed or fail together. Rolls back if any operation fails.
     *
     * <p><b>After Clearing:</b></p>
     * The user will be navigated back to the Setup screen to configure a new
     * budget cycle, PIN, and categories.
     */
    public void clearCycle() {
        String deleteTransactionsSql = "DELETE FROM Transactions";
        String deleteCategoriesSql = "DELETE FROM Category";
        String deleteBudgetSql = "DELETE FROM Budget";
        String updateStudentSql = "UPDATE Student SET student_state = 'INACTIVE'";

        try {
            connection.setAutoCommit(false);

            // Delete all transactions
            try (PreparedStatement stmt = connection.prepareStatement(deleteTransactionsSql)) {
                stmt.executeUpdate();
            }
            // Delete all categories
            try (PreparedStatement stmt = connection.prepareStatement(deleteCategoriesSql)) {
                stmt.executeUpdate();
            }
            // Delete the current budget
            try (PreparedStatement stmt = connection.prepareStatement(deleteBudgetSql)) {
                stmt.executeUpdate();
            }
            // Set student state to INACTIVE
            try (PreparedStatement stmt = connection.prepareStatement(updateStudentSql)) {
                stmt.executeUpdate();
            }

            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Retrieves the current budget cycle.
     * <p>
     * <b>Note:</b> This method is currently not implemented and always returns
     * {@code null}. Future enhancements should implement this method to retrieve
     * the existing budget from the database.
     * </p>
     *
     * @return {@code null} (implementation pending)
     */
    public Object getCycle() {
        // TODO: Implement method to retrieve existing budget from database
        return null;
    }
}