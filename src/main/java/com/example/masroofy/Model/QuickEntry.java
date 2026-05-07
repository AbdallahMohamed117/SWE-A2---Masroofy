package com.example.masroofy.Model;

import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class for handling quick expense entry and category management.
 * <p>
 * The {@code QuickEntry} class manages the creation of new expense transactions
 * and the management of expense categories. It handles all database operations
 * required to add transactions, update budgets, adjust daily limits, and manage
 * categories.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Adding new expense transactions with budget validation</li>
 *   <li>Automatically creating new categories when they don't exist</li>
 *   <li>Updating budget allowance after each transaction</li>
 *   <li>Updating daily safe limit after each transaction</li>
 *   <li>Managing expense categories (add, remove, retrieve)</li>
 * </ul>
 *
 * <p><b>Database Tables Used:</b></p>
 * <ul>
 *   <li>{@code Budget} - Stores allowance and daily safe limit</li>
 *   <li>{@code Student} - Stores student information with active state</li>
 *   <li>{@code Category} - Stores expense categories</li>
 *   <li>{@code Transactions} - Stores individual transaction records</li>
 * </ul>
 *
 * <p><b>Transaction Flow:</b></p>
 * <ol>
 *   <li>Validate transaction amount (must be > 0)</li>
 *   <li>Get the active student ID</li>
 *   <li>Check if transaction amount fits within remaining allowance</li>
 *   <li>Get or create the transaction category</li>
 *   <li>Update budget allowance (subtract transaction amount)</li>
 *   <li>Update daily safe limit (subtract transaction amount)</li>
 *   <li>Insert the transaction record</li>
 *   <li>Commit all changes (or rollback on failure)</li>
 * </ol>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractModel
 * @see Transaction
 * @see Category
 * @see com.example.masroofy.Controller.QuickEntryController
 */
public class QuickEntry extends AbstractModel {

    /**
     * Constructs a QuickEntry model instance.
     * <p>
     * Initializes the database connection through the parent {@link AbstractModel}
     * constructor.
     * </p>
     */
    public QuickEntry() {
        super();
    }

    /**
     * Adds a new expense transaction to the database.
     * <p>
     * This method performs a comprehensive set of operations to add a transaction:
     * <ul>
     *   <li>Validates that the transaction amount is positive</li>
     *   <li>Retrieves the active student ID</li>
     *   <li>Verifies sufficient allowance exists for the transaction</li>
     *   <li>Finds existing category or creates a new one</li>
     *   <li>Updates the budget allowance (decreases by transaction amount)</li>
     *   <li>Updates the daily safe limit (decreases by transaction amount)</li>
     *   <li>Inserts the transaction record</li>
     * </ul>
     * </p>
     *
     * <p><b>Transaction Safety:</b></p>
     * Uses database transactions with auto-commit disabled to ensure all
     * operations succeed or fail together. Rolls back if any operation fails.
     *
     * <p><b>Failure Conditions:</b></p>
     * <ul>
     *   <li>Transaction amount ≤ 0</li>
     *   <li>No active student found</li>
     *   <li>Transaction amount exceeds remaining allowance</li>
     *   <li>Any database operation fails</li>
     * </ul>
     *
     * @param transaction the transaction to add (contains amount, category, and timestamp)
     * @return {@code true} if the transaction was added successfully,
     *         {@code false} otherwise
     */
    public boolean addTransaction(Transaction transaction) {
        String getAllowanceQuery = "SELECT allowance FROM Budget";
        String updateAllowanceQuery = "UPDATE Budget SET allowance = allowance - ?";
        String updateDailyLimitQuery = "UPDATE Budget SET daily_safe_limit = daily_safe_limit - ?";
        String studentIdQuery         = "SELECT student_id FROM Student WHERE student_state = 'ACTIVE'";
        String categoryIdQuery        = "SELECT category_id FROM Category WHERE category_name = ?";
        String insertCategoryQuery    = "INSERT INTO Category (category_name) VALUES (?)";
        String insertTransactionQuery = "INSERT INTO Transactions (transaction_amount, transaction_timestamp, student_id, category_id) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // Validate transaction amount
            double transactionAmount = transaction.getTransactionAmount();
            if (transactionAmount <= 0) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            // Get active student ID
            int studentId = -1;
            try (PreparedStatement studentStatement = connection.prepareStatement(studentIdQuery)) {
                ResultSet studentResult = studentStatement.executeQuery();
                if (studentResult.next()) {
                    studentId = studentResult.getInt("student_id");
                }
            }
            if (studentId == -1) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

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

            if (transactionAmount > currentAllowance) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            // Get or create category
            int categoryId = -1;
            String categoryName = transaction.getTransactionCategory().getCategoryName();
            try (PreparedStatement getCategoryIdStatement = connection.prepareStatement(categoryIdQuery)) {
                getCategoryIdStatement.setString(1, categoryName);
                ResultSet categoryResult = getCategoryIdStatement.executeQuery();
                if (categoryResult.next()) {
                    categoryId = categoryResult.getInt("category_id");
                }
            }

            // Create new category if it doesn't exist
            if (categoryId == -1) {
                try (PreparedStatement insertCategoryStatement = connection.prepareStatement(insertCategoryQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    insertCategoryStatement.setString(1, categoryName);
                    insertCategoryStatement.executeUpdate();
                    ResultSet generatedKeys = insertCategoryStatement.getGeneratedKeys();
                    generatedKeys.next();
                    categoryId = (int) generatedKeys.getLong(1);
                }
            }

            // Update allowance
            try (PreparedStatement updateAllowanceStmt = connection.prepareStatement(updateAllowanceQuery)) {
                updateAllowanceStmt.setDouble(1, transactionAmount);
                updateAllowanceStmt.executeUpdate();
            }

            // Update daily safe limit
            try (PreparedStatement updateDailyLimitStmt = connection.prepareStatement(updateDailyLimitQuery)) {
                updateDailyLimitStmt.setDouble(1, transactionAmount);
                updateDailyLimitStmt.executeUpdate();
            }

            // Insert transaction
            try (PreparedStatement insertTransactionStatement = connection.prepareStatement(insertTransactionQuery)) {
                insertTransactionStatement.setDouble(1, transactionAmount);
                insertTransactionStatement.setLong  (2, transaction.getTransactionTimestamp());
                insertTransactionStatement.setInt   (3, studentId);
                insertTransactionStatement.setInt   (4, categoryId);
                insertTransactionStatement.executeUpdate();
            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;

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
     * Adds a new expense category to the database.
     * <p>
     * This method checks if a category with the same name already exists before
     * inserting. If a duplicate is found, the operation fails.
     * </p>
     *
     * @param category the category to add
     * @return {@code true} if the category was added successfully,
     *         {@code false} if the category already exists or a database error occurs
     */
    public boolean addCategory(Category category) {
        String checkCategoryQuery = "SELECT COUNT(*) FROM Category WHERE category_name = ?";
        String insertCategoryQuery = "INSERT INTO Category (category_name) VALUES (?)";

        // Check if category already exists
        try (PreparedStatement checkStmt = connection.prepareStatement(checkCategoryQuery)) {
            checkStmt.setString(1, category.getCategoryName());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Category already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Insert the new category
        try (PreparedStatement insertCategoryStatement = connection.prepareStatement(insertCategoryQuery)) {
            insertCategoryStatement.setString(1, category.getCategoryName());
            insertCategoryStatement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes an expense category from the database.
     * <p>
     * <b>Warning:</b> This method does not check if the category is currently
     * being used by any transactions. Removing a category that has associated
     * transactions could cause referential integrity issues.
     * </p>
     *
     * @param category the category to remove
     */
    public void removeCategory(Category category) {
        String deleteCategoryQuery = "DELETE FROM Category WHERE category_name = ?";

        try (PreparedStatement deleteCategoryStatement = connection.prepareStatement(deleteCategoryQuery)) {
            deleteCategoryStatement.setString(1, category.getCategoryName());
            deleteCategoryStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all expense categories from the database.
     *
     * @return a {@link List} of {@link Category} objects containing all categories
     */
    public List<Category> getCategories() {
        String getCategoriesQuery = "SELECT * FROM Category";
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement getCategoriesStatement = connection.prepareStatement(getCategoriesQuery);
             ResultSet resultSet = getCategoriesStatement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category();
                category.setCategoryName(resultSet.getString("category_name"));
                categories.add(category);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
}