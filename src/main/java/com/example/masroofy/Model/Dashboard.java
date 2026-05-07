package com.example.masroofy.Model;

import com.example.masroofy.Database.DatabaseConnection;
import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;

import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Model class for managing dashboard data and calculations.
 * <p>
 * The {@code Dashboard} class provides all financial data required for the
 * dashboard screen, including allowance, daily spending limits, transaction
 * data for pie charts, and days remaining in the budget cycle. It also handles
 * daily limit recalculation when a new day begins.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Retrieving the total allowance from the budget</li>
 *   <li>Getting the current daily safe spending limit</li>
 *   <li>Fetching transaction data for pie chart visualization</li>
 *   <li>Calculating days remaining in the budget period</li>
 *   <li>Recalculating daily limits when a new day starts</li>
 *   <li>Detecting when the daily limit has been exceeded</li>
 * </ul>
 *
 * <p><b>Database Tables Used:</b></p>
 * <ul>
 *   <li>{@code Budget} - Stores allowance, daily limits, and date information</li>
 *   <li>{@code Transactions} - Stores individual transaction records</li>
 *   <li>{@code Category} - Stores category names for transaction classification</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractModel
 * @see Transaction
 * @see Category
 * @see com.example.masroofy.Controller.DashboardController
 */
public class Dashboard extends AbstractModel {

    /**
     * Constructs a Dashboard model instance.
     * <p>
     * Initializes the database connection through the parent {@link AbstractModel}
     * constructor.
     * </p>
     */
    public Dashboard() {
        super();
    }

    /**
     * Retrieves the total allowance from the budget.
     *
     * @return the allowance amount, or {@code 0} if no budget exists or an error occurs
     */
    public double getAllowance() {
        String query = "SELECT allowance FROM Budget";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return result.getDouble("allowance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Retrieves the current daily safe spending limit.
     *
     * @return the daily safe limit, or {@code 0} if no limit exists or an error occurs
     */
    public double getDailyLimit() {
        String getDailyLimitQuery = "SELECT daily_safe_limit FROM Budget";

        try (PreparedStatement prepareDailyLimit = connection.prepareStatement(getDailyLimitQuery)) {
            ResultSet resultDailyLimit = prepareDailyLimit.executeQuery();
            double dailyLimit = resultDailyLimit.getDouble("daily_safe_limit");
            return dailyLimit;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Daily Limit Doesn't exist
        return 0;
    }

    /**
     * Retrieves all transaction data for pie chart visualization.
     * <p>
     * This method fetches all transactions from the database along with their
     * associated category names. The returned list is used by the dashboard
     * controller to calculate category percentages and build the pie chart.
     * </p>
     *
     * @return a {@link List} of {@link Transaction} objects containing amount,
     *         timestamp, and category information
     */
    public List<Transaction> getPiechartData() {

        StringBuilder query = new StringBuilder(
                "SELECT t.transaction_amount, t.transaction_timestamp, c.category_name " +
                        "FROM Transactions t JOIN Category c ON t.category_id = c.category_id " +
                        "WHERE 1=1 "
        );

        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionAmount(result.getDouble("transaction_amount"));
                transaction.setTransactionTimestamp(result.getLong("transaction_timestamp"));

                Category c = new Category();
                c.setCategoryName(result.getString("category_name"));
                transaction.setTransactionCategory(c);

                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    /**
     * Calculates the number of days remaining in the current budget cycle.
     * <p>
     * This method retrieves the end date from the Budget table and calculates
     * the difference between today and that end date.
     * </p>
     *
     * @return the number of days remaining (minimum {@code 0}), or {@code 0} if
     *         an error occurs or the budget period has ended
     */
    public int getDaysLeft() {
        String getEndDateQuery = "SELECT end_date FROM Budget";
        try (PreparedStatement prepareEndDate = connection.prepareStatement(getEndDateQuery)) {
            ResultSet result = prepareEndDate.executeQuery();
            if (result.next()) {
                String endDateStr = result.getString("end_date");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = sdf.parse(endDateStr);
                LocalDate endDate = parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate today = LocalDate.now();
                long days = ChronoUnit.DAYS.between(today, endDate);
                return (int) Math.max(days, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Recalculates the daily spending limit when a new day begins.
     * <p>
     * This method checks if the daily limit needs to be recalculated by comparing
     * the last recalculation date with today's date. If no recalculation has
     * occurred today, it computes a new daily limit based on the remaining
     * allowance and days left in the budget cycle.
     * </p>
     *
     * <p><b>Recalculation Logic:</b></p>
     * <ol>
     *   <li>Get the last recalculation date from the Budget table</li>
     *   <li>If no recalculation has been done today, proceed</li>
     *   <li>Calculate days remaining in the budget period</li>
     *   <li>If days remaining > 0, compute new daily limit: {@code allowance / daysLeft}</li>
     *   <li>Update the {@code daily_safe_limit}, {@code original_daily_limit}, and {@code last_recalc_date} in the database</li>
     * </ol>
     *
     * <p><b>Note:</b></p>
     * This method should be called each time the dashboard is refreshed to ensure
     * the daily limit is current.
     */
    public void recalculateDailyLimitIfNewDay() {
        String checkRecalcQuery = "SELECT last_recalc_date, daily_safe_limit FROM Budget";
        String updateDailyLimitQuery = "UPDATE Budget SET daily_safe_limit = ?, original_daily_limit = ?, last_recalc_date = ?";
        String getAllowanceQuery = "SELECT allowance FROM Budget";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new java.util.Date());

            PreparedStatement checkStmt = connection.prepareStatement(checkRecalcQuery);
            ResultSet result = checkStmt.executeQuery();

            if (result.next()) {
                String lastRecalcDate = result.getString("last_recalc_date");

                if (lastRecalcDate == null || !lastRecalcDate.equals(today)) {
                    double currentAllowance = 0;
                    try (PreparedStatement getAllowanceStmt = connection.prepareStatement(getAllowanceQuery)) {
                        ResultSet allowanceRs = getAllowanceStmt.executeQuery();
                        if (allowanceRs.next()) {
                            currentAllowance = allowanceRs.getDouble("allowance");
                        }
                    }

                    int daysLeft = getDaysLeft();
                    if (daysLeft > 0) {
                        double newDailyLimit = currentAllowance / daysLeft;

                        try (PreparedStatement updateStmt = connection.prepareStatement(updateDailyLimitQuery)) {
                            updateStmt.setDouble(1, newDailyLimit);
                            updateStmt.setDouble(2, newDailyLimit);
                            updateStmt.setString(3, today);
                            updateStmt.executeUpdate();
                        }
                    }
                }
            }
            checkStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks whether the daily spending limit has been exceeded.
     * <p>
     * A daily limit is considered exceeded when the remaining daily safe limit
     * is zero or negative, indicating that the user has spent more than the
     * allocated amount for the day.
     * </p>
     *
     * @return {@code true} if the daily limit is exceeded (daily_safe_limit ≤ 0),
     *         {@code false} otherwise
     */
    public boolean isDailyLimitExceeded() {
        String query = "SELECT daily_safe_limit FROM Budget";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                double dailyLimit = result.getDouble("daily_safe_limit");
                return dailyLimit <= 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}