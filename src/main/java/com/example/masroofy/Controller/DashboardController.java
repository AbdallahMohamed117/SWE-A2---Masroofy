package com.example.masroofy.Controller;

import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.*;
import com.example.masroofy.View.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the Dashboard screen in the Masroofy application.
 * <p>
 * The {@code DashboardController} is responsible for managing the main dashboard view,
 * which displays financial summaries including daily allowance, total spent,
 * remaining days, and a pie chart showing expense distribution by category.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Calculating and updating daily spending limits</li>
 *   <li>Processing transactions to generate category-based expense percentages</li>
 *   <li>Detecting and alerting when daily limits are exceeded</li>
 *   <li>Managing pie chart data and progress bar visualization</li>
 *   <li>Checking if a new day has started to recalculate daily limits</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractController
 * @see Dashboard
 * @see DashboardView
 * @see History
 */
public class DashboardController implements AbstractController {

    /** The dashboard model containing financial data and business logic. */
    private Dashboard model;

    /** The dashboard view responsible for UI display and updates. */
    private DashboardView view;

    /** The history model providing access to transaction data. */
    private History historyModel;

    /**
     * Constructs a {@code DashboardController} with the specified model and view.
     * <p>
     * Initializes the controller, creates a new {@link History} model instance,
     * and immediately refreshes the dashboard to display current data.
     * </p>
     *
     * @param m the dashboard model containing financial data
     * @param v the dashboard view for UI interaction
     * @throws NullPointerException if {@code m} or {@code v} is {@code null}
     */
    public DashboardController(Dashboard m, DashboardView v) {
        if (m == null) {
            throw new NullPointerException("Dashboard model cannot be null");
        }
        if (v == null) {
            throw new NullPointerException("Dashboard view cannot be null");
        }
        view = v;
        model = m;
        historyModel = new History();
        printView();
    }

    /**
     * Refreshes the dashboard view by calling {@link #refreshDashboard()}.
     * <p>
     * This method implements the {@link AbstractController} interface requirement.
     * </p>
     */
    @Override
    public void printView() {
        refreshDashboard();
    }

    /**
     * Refreshes all dashboard data and updates the UI.
     * <p>
     * This method performs the following operations:
     * <ol>
     *   <li>Sets the remaining days in the month on the view</li>
     *   <li>Checks if a new day has started and recalculates daily limits if needed</li>
     *   <li>Retrieves all transactions from the history model</li>
     *   <li>Calculates total spent amount</li>
     *   <li>Computes category percentages for the pie chart</li>
     *   <li>Calculates progress based on allowance vs. total spent</li>
     *   <li>Detects overspending conditions</li>
     *   <li>Updates all UI components with calculated values</li>
     *   <li>Shows or hides alerts based on limit status</li>
     * </ol>
     *
     * <p><b>Pie Chart Calculation:</b></p>
     * Each transaction's percentage is calculated as:
     * <pre>(transaction amount / allowance) * 100</pre>
     * Percentages are aggregated by category using {@link Map#merge}.
     *
     * <p><b>Overspending Detection:</b></p>
     * An overspend is flagged when {@code totalSpent > dailyLimit}.
     */
    public void refreshDashboard() {
        Map<String, Double> pieChart = new HashMap<>();
        double progress = 0;
        boolean isOverspent = false;
        double totalSpent = 0;

        // Set days remaining
        view.setDaysLeft(model.getDaysLeft());

        // Recalculate daily limit if a new day has started
        model.recalculateDailyLimitIfNewDay();

        double dailyLimit = model.getDailyLimit();
        double allowance = model.getAllowance();

        view.setDailyLimit(dailyLimit);
        view.setDaysLeft(model.getDaysLeft());

        // Process all transactions
        List<Transaction> amounts = historyModel.getTransactions();
        if (!amounts.isEmpty()) {
            for (Transaction t : amounts) {
                totalSpent += t.getTransactionAmount();
                Category category = t.getTransactionCategory();
                String categoryName = category.getCategoryName();

                if (allowance > 0) {
                    double percentage = (t.getTransactionAmount() / allowance) * 100;
                    pieChart.merge(categoryName, percentage, Double::sum);
                } else {
                    pieChart.merge(categoryName, 0.0, Double::sum);
                }
            }

            // Calculate progress and check overspending
            progress = totalSpent / allowance;
            isOverspent = totalSpent > dailyLimit;
        }

        // Update view with calculated data
        view.setTotalSpent(totalSpent);
        view.setPieChart(pieChart);
        view.setProgressBar(progress);
        view.updatePieChartProgress(progress);
        view.setPieChartTotal(totalSpent);
        view.showLimitColor(isOverspent);
        view.setStatusIcon(isOverspent);

        // Handle daily limit exceeded alert
        if (model.isDailyLimitExceeded()) {
            view.showLimitExceededAlert();
        } else {
            view.hideLimitExceededAlert();
        }

        view.printScreen();
    }
}