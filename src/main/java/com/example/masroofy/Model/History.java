package com.example.masroofy.Model;

import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;
import com.example.masroofy.util.DateUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Model class for managing transaction history in the Masroofy application.
 * <p>
 * The {@code History} class provides comprehensive database operations for
 * retrieving, editing, and deleting financial transactions. It supports
 * filtering by category and date range, transaction editing with allowance
 * adjustment, and transaction deletion with budget recalculation.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Retrieving all transactions with optional category and date filters</li>
 *   <li>Editing existing transactions with automatic allowance and daily limit updates</li>
 *   <li>Deleting transactions with budget readjustment</li>
 *   <li>Fetching available category names for filtering</li>
 * </ul>
 *
 * <p><b>Database Tables Used:</b></p>
 * <ul>
 *   <li>{@code Transactions} - Stores transaction amounts and timestamps</li>
 *   <li>{@code Category} - Stores category names and IDs</li>
 *   <li>{@code Budget} - Stores allowance and daily limits for adjustment</li>
 * </ul>
 *
 * <p><b>Transaction Editing Logic:</b></p>
 * <ul>
 *   <li>Increases allowance when a transaction amount is decreased</li>
 *   <li>Decreases allowance when a transaction amount is increased (subject to availability)</li>
 *   <li>Updates daily safe limit if the transaction occurred today</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractModel
 * @see Transaction
 * @see Category
 * @see DateUtil
 * @see com.example.masroofy.Controller.HistoryController
 */
public class History extends AbstractModel {

    /**
     * Constructs a History model instance.
     * <p>
     * Initializes the database connection through the parent {@link AbstractModel}
     * constructor.
     * </p>
     */
    public History() {
        super();
    }

    /**
     * Retrieves all transactions without any filtering.
     * <p>
     * This is a convenience method that calls {@link #getTransactions(String, Date, Date)}
     * with all parameters set to {@code null}.
     * </p>
     *
     * @return a {@link List} of all {@link Transaction} objects
     */
    public List<Transaction> getTransactions() {
        return getTransactions(null, null, null);
    }

    /**
     * Retrieves transactions filtered by category and/or date range.
     * <p>
     * This method builds a dynamic SQL query based on which filter parameters
     * are provided. Only non-null parameters are applied as filters.
     * </p>
     *
     * <p><b>Filter Behavior:</b></p>
     * <ul>
     *   <li>If {@code category} is {@code null}, all categories are included</li>
     *   <li>If {@code from} is {@code null}, no lower date bound is applied</li>
     *   <li>If {@code to} is {@code null}, no upper date bound is applied</li>
     * </ul>
     *
     * @param category the category name to filter by, or {@code null} for all categories
     * @param from the start date for filtering, or {@code null} for no lower bound
     * @param to the end date for filtering, or {@code null} for no upper bound
     * @return a {@link List} of {@link Transaction} objects matching the filters
     */
    public List<Transaction> getTransactions(String category, Date from, Date to) {

        StringBuilder query = new StringBuilder(
                "SELECT t.transaction_amount, t.transaction_timestamp, c.category_name " +
                        "FROM Transactions t JOIN Category c ON t.category_id = c.category_id " +
                        "WHERE 1=1 "
        );

        if (category != null) query.append("AND c.category_name = ? ");
        if (from     != null) query.append("AND t.transaction_timestamp >= ? ");
        if (to       != null) query.append("AND t.transaction_timestamp <= ? ");

        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {

            int index = 1;
            if (category != null) stmt.setString(index++, category);
            if (from     != null) stmt.setLong  (index++, from.getTime());
            if (to       != null) stmt.setLong  (index++, to.getTime());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionAmount   (result.getDouble("transaction_amount"));
                transaction.setTransactionTimestamp(result.getLong  ("transaction_timestamp"));

                Category c = new Category();
                c.setCategoryName(result.getString("category_name"));
                transaction.setTransactionCategory(c);

                transactions.add(transaction);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Edits an existing transaction with allowance and daily limit adjustments.
     * <p>
     * This method performs a comprehensive update of a transaction, including:
     * <ul>
     *   <li>Calculating the difference between old and new amounts</li>
     *   <li>Adjusting the budget allowance accordingly</li>
     *   <li>Updating the daily safe limit if the transaction occurred today</li>
     *   <li>Ensuring the new amount does not exceed available allowance</li>
     * </ul>
     *
     * <p><b>Allowance Adjustment Logic:</b></p>
     * <ul>
     *   <li>If amount increases (delta &gt; 0): Decrease allowance by delta</li>
     *   <li>If amount decreases (delta &lt; 0): Increase allowance by absolute delta</li>
     *   <li>If delta exceeds available allowance: Transaction fails (rollback)</li>
     * </ul>
     *
     * <p><b>Transaction Properties:</b></p>
     * <ul>
     *   <li>Uses database transactions with auto-commit disabled</li>
     *   <li>Rolls back all changes if any operation fails</li>
     *   <li>Validates that new amount is positive (> 0)</li>
     * </ul>
     *
     * @param transaction the transaction with updated values (timestamp identifies the original)
     * @return {@code true} if the edit was successful, {@code false} otherwise
     */
    public boolean editTransaction(Transaction transaction) {
        String getOldTransactionQuery = "SELECT transaction_amount FROM Transactions WHERE transaction_timestamp = ?";
        String getAllowanceQuery = "SELECT allowance FROM Budget";
        String updateAllowanceQuery = "UPDATE Budget SET allowance = allowance - ?";
        String addAllowanceQuery = "UPDATE Budget SET allowance = allowance + ?";
        String categoryIdQuery = "SELECT category_id FROM Category WHERE category_name = ?";
        String updateTransactionQuery = "UPDATE Transactions SET transaction_amount = ?, category_id = ? WHERE transaction_timestamp = ?";

        try {
            connection.setAutoCommit(false);

            // Get the original transaction amount
            double oldAmount = 0;
            try (PreparedStatement getOldStmt = connection.prepareStatement(getOldTransactionQuery)) {
                getOldStmt.setLong(1, transaction.getTransactionTimestamp());
                ResultSet oldRs = getOldStmt.executeQuery();
                if (oldRs.next()) {
                    oldAmount = oldRs.getDouble("transaction_amount");
                } else {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }

            double newAmount = transaction.getTransactionAmount();
            if (newAmount <= 0) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            double delta = newAmount - oldAmount;

            // Adjust allowance based on amount change
            if (delta > 0) {
                // Check if enough allowance is available
                double currentAllowance = 0;
                try (PreparedStatement getAllowanceStmt = connection.prepareStatement(getAllowanceQuery)) {
                    ResultSet allowanceRs = getAllowanceStmt.executeQuery();
                    if (allowanceRs.next()) {
                        currentAllowance = allowanceRs.getDouble("allowance");
                    } else {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        return false;
                    }
                }
                if (delta > currentAllowance) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
                try (PreparedStatement updateAllowanceStmt = connection.prepareStatement(updateAllowanceQuery)) {
                    updateAllowanceStmt.setDouble(1, delta);
                    updateAllowanceStmt.executeUpdate();
                }
            }
            else if (delta < 0) {
                try (PreparedStatement addAllowanceStmt = connection.prepareStatement(addAllowanceQuery)) {
                    addAllowanceStmt.setDouble(1, Math.abs(delta));
                    addAllowanceStmt.executeUpdate();
                }
            }

            // Adjust daily safe limit if transaction occurred today
            if (DateUtil.isToday(transaction.getTransactionTimestamp())) {
                String adjustDailyLimitQuery = "UPDATE Budget SET daily_safe_limit = daily_safe_limit + ?";
                try (PreparedStatement adjustDailyLimitStmt = connection.prepareStatement(adjustDailyLimitQuery)) {
                    adjustDailyLimitStmt.setDouble(1, -delta);
                    adjustDailyLimitStmt.executeUpdate();
                }
            }

            // Get category ID for the new category
            int categoryId = -1;
            try (PreparedStatement categoryIdStatement = connection.prepareStatement(categoryIdQuery)) {
                categoryIdStatement.setString(1, transaction.getTransactionCategory().getCategoryName());
                ResultSet categoryResult = categoryIdStatement.executeQuery();
                if (categoryResult.next()) {
                    categoryId = categoryResult.getInt("category_id");
                }
            }
            if (categoryId == -1) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            // Update the transaction
            try (PreparedStatement updateTransactionStatement = connection.prepareStatement(updateTransactionQuery)) {
                updateTransactionStatement.setDouble(1, newAmount);
                updateTransactionStatement.setInt   (2, categoryId);
                updateTransactionStatement.setLong  (3, transaction.getTransactionTimestamp());
                int rows = updateTransactionStatement.executeUpdate();

                connection.commit();
                connection.setAutoCommit(true);
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Deletes a transaction and adjusts the budget accordingly.
     * <p>
     * This method removes a transaction from the database and adds the
     * transaction amount back to the budget allowance. If the transaction
     * occurred today, the daily safe limit is also increased by the transaction
     * amount.
     * </p>
     *
     * <p><b>Delete Operations:</b></p>
     * <ul>
     *   <li>Retrieves the transaction amount before deletion</li>
     *   <li>Adds the amount back to the budget allowance</li>
     *   <li>If transaction occurred today, increases daily safe limit by the amount</li>
     *   <li>Deletes the transaction record from the database</li>
     * </ul>
     *
     * <p><b>Transaction Safety:</b></p>
     * Uses database transactions with auto-commit disabled to ensure all
     * operations succeed or fail together. Rolls back if any operation fails.
     *
     * @param transaction the transaction to be deleted
     * @return {@code true} if the deletion was successful, {@code false} otherwise
     */
    public boolean deleteTransaction(Transaction transaction) {
        String getTransactionAmountQuery = "SELECT transaction_amount FROM Transactions WHERE transaction_timestamp = ?";
        String deleteTransactionQuery = "DELETE FROM Transactions WHERE transaction_timestamp = ?";
        String addAllowanceQuery = "UPDATE Budget SET allowance = allowance + ?";

        try {
            connection.setAutoCommit(false);

            // Get the transaction amount before deletion
            double amount = 0;
            try (PreparedStatement getAmountStmt = connection.prepareStatement(getTransactionAmountQuery)) {
                getAmountStmt.setLong(1, transaction.getTransactionTimestamp());
                ResultSet amountRs = getAmountStmt.executeQuery();
                if (amountRs.next()) {
                    amount = amountRs.getDouble("transaction_amount");
                } else {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }

            // Add the amount back to allowance
            try (PreparedStatement addAllowanceStmt = connection.prepareStatement(addAllowanceQuery)) {
                addAllowanceStmt.setDouble(1, amount);
                addAllowanceStmt.executeUpdate();
            }

            // Adjust daily safe limit if transaction occurred today
            if (DateUtil.isToday(transaction.getTransactionTimestamp())) {
                String adjustDailyLimitQuery = "UPDATE Budget SET daily_safe_limit = daily_safe_limit + ?";
                try (PreparedStatement adjustDailyLimitStmt = connection.prepareStatement(adjustDailyLimitQuery)) {
                    adjustDailyLimitStmt.setDouble(1, amount);
                    adjustDailyLimitStmt.executeUpdate();
                }
            }

            // Delete the transaction
            try (PreparedStatement deleteTransactionStatement = connection.prepareStatement(deleteTransactionQuery)) {
                deleteTransactionStatement.setLong(1, transaction.getTransactionTimestamp());
                int rows = deleteTransactionStatement.executeUpdate();

                connection.commit();
                connection.setAutoCommit(true);
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Retrieves all available category names from the database.
     * <p>
     * This method fetches the list of category names used for filtering
     * transactions in the History screen.
     * </p>
     *
     * @return a {@link List} of category name strings
     */
    public List<String> getCategories() {
        String getCategoriesQuery = "SELECT category_name FROM Category";
        List<String> categories = new ArrayList<>();
        try(PreparedStatement getCategoryStatement = connection.prepareStatement(getCategoriesQuery)) {
            ResultSet result = getCategoryStatement.executeQuery();
            while(result.next()) {
                categories.add(result.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}