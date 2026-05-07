package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import java.util.Map;
import javafx.geometry.Insets;

/**
 * View class for the Dashboard screen in the Masroofy application.
 * <p>
 * The {@code DashboardView} manages the main dashboard UI, displaying financial
 * summaries including daily limit, total spent, days remaining, a circular
 * progress indicator, pie chart representation, and category breakdown.
 * </p>
 *
 * <p><b>UI Components:</b></p>
 * <ul>
 *   <li>Daily limit display with color coding for overspending</li>
 *   <li>Days remaining in budget cycle</li>
 *   <li>Status icon (checkmark for on-track, warning for overspent)</li>
 *   <li>Circular progress bar showing spending percentage</li>
 *   <li>Category insights with emoji icons and percentage breakdown</li>
 *   <li>Alert banner for daily limit exceeded warnings</li>
 *   <li>Navigation buttons for Quick Entry and History screens</li>
 * </ul>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Dynamic color coding based on spending status</li>
 *   <li>Circular progress indicator with stroke dash array manipulation</li>
 *   <li>Category-specific emoji mapping for visual recognition</li>
 *   <li>Alert system for limit exceeded notifications</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractView
 * @see com.example.masroofy.Controller.DashboardController
 */
public class DashboardView implements AbstractView {

    // FXML Injected Components
    @FXML private Label tvDailyLimit;
    @FXML private Label lblDaysLeft;
    @FXML private Label lblStatusIcon;
    @FXML private Label lblSpent;
    @FXML private Label lblLimit;
    @FXML private Label lblPieChartTotal;
    @FXML private Button btnLogExpense;
    @FXML private Button btnHistory;
    @FXML private ProgressBar progressBar;
    @FXML private Circle circleUsed;
    @FXML private Circle circleRemaining;
    @FXML private VBox categoryContainer;
    @FXML private VBox dailyLimitCard;
    @FXML private Label btnSettings;

    private VBox alertBanner;
    private Runnable onNavigateToQuickEntry;
    private Runnable onNavigateToHistory;
    private Runnable onNavigateToSettings;
    private double dailyLimit;
    private double totalSpent;
    private Map<String, Double> categoryData;

    /**
     * Sets the callback for navigating to the Quick Entry screen.
     *
     * @param r the Runnable to execute when Quick Entry navigation is triggered
     */
    public void setOnNavigateToQuickEntry(Runnable r) {
        this.onNavigateToQuickEntry = r;
    }

    /**
     * Sets the callback for navigating to the History screen.
     *
     * @param r the Runnable to execute when History navigation is triggered
     */
    public void setOnNavigateToHistory(Runnable r) {
        this.onNavigateToHistory = r;
    }

    /**
     * Refreshes the dashboard display by updating all UI components.
     * <p>
     * This method updates the daily limit display, pie chart, and category insights.
     * </p>
     */
    @Override
    public void printScreen() {
        showSafeDailyLimit();
        showPieChart();
        showCategoryInsights();
    }

    /**
     * Sets the number of days remaining in the budget cycle.
     *
     * @param days the number of days remaining
     */
    public void setDaysLeft(int days) {
        if (lblDaysLeft != null) {
            lblDaysLeft.setText(days + " days left");
        }
    }

    /**
     * Sets the status icon based on spending status.
     * <p>
     * Displays a checkmark (✅) when on-track or a warning symbol (⚠️) when overspent.
     * </p>
     *
     * @param isOverspent {@code true} to show warning icon, {@code false} for checkmark
     */
    public void setStatusIcon(boolean isOverspent) {
        if (lblStatusIcon != null) {
            lblStatusIcon.setText(isOverspent ? "\u26a0\ufe0f" : "\u2705");
        }
    }

    /**
     * Sets the callback for navigating to the Settings screen.
     *
     * @param r the Runnable to execute when Settings navigation is triggered
     */
    public void setOnNavigateToSettings(Runnable r) {
        this.onNavigateToSettings = r;
        if (btnSettings != null) {
            btnSettings.setOnMouseClicked(e -> onNavigateToSettings.run());
        }
    }

    /**
     * Updates the daily limit display area with current values.
     * <p>
     * Shows the daily limit amount, total spent, and limit label.
     * </p>
     */
    public void showSafeDailyLimit() {
        tvDailyLimit.setText("EGP " + String.format("%.2f", dailyLimit));
        lblSpent.setText("Spent: EGP " + String.format("%.2f", totalSpent));
        lblLimit.setText("Limit: EGP " + String.format("%.2f", dailyLimit));
    }

    /**
     * Sets the daily limit value.
     *
     * @param limit the daily spending limit
     */
    public void setDailyLimit(double limit) {
        this.dailyLimit = limit;
    }

    /**
     * Sets the total amount spent.
     *
     * @param spent the total spending amount
     */
    public void setTotalSpent(double spent) {
        this.totalSpent = spent;
    }

    /**
     * Sets the progress bar value.
     *
     * @param progress the progress value (0.0 to 1.0, clamped automatically)
     */
    public void setProgressBar(double progress) {
        if (progressBar != null) {
            progressBar.setProgress(Math.min(progress, 1.0));
        }
    }

    /**
     * Sets the total amount displayed on the pie chart.
     *
     * @param total the total amount for the pie chart
     */
    public void setPieChartTotal(double total) {
        if (lblPieChartTotal != null) {
            lblPieChartTotal.setText("EGP " + String.format("%.2f", total));
        }
    }

    /**
     * Updates the circular progress indicator based on spending progress.
     * <p>
     * This method manipulates the stroke dash arrays of two circles to create
     * a circular progress indicator effect.
     * </p>
     *
     * @param progress the spending progress (0.0 to 1.0)
     */
    public void updatePieChartProgress(double progress) {
        if (circleUsed == null || circleRemaining == null) return;

        double circumference = 2 * Math.PI * 55.0;
        double clampedProgress = Math.max(0, Math.min(progress, 1.0));

        if (clampedProgress <= 0) {
            circleUsed.getStrokeDashArray().clear();
            circleRemaining.getStrokeDashArray().clear();
            return;
        }

        double usedDash = clampedProgress * circumference;
        double usedGap = circumference - usedDash;
        circleUsed.getStrokeDashArray().setAll(usedDash, usedGap);

        double remainDash = (1 - clampedProgress) * circumference;
        double remainGap = circumference - remainDash;
        circleRemaining.getStrokeDashArray().setAll(remainDash, remainGap);
        circleRemaining.setStrokeDashOffset(-usedDash);
    }

    /**
     * Displays the pie chart (placeholder method).
     * <p>
     * <b>Note:</b> This method is currently empty. Future implementation
     * should render the actual pie chart visualization.
     * </p>
     */
    public void showPieChart() {
        // TODO: Implement pie chart visualization
    }

    /**
     * Sets the pie chart data.
     *
     * @param data a Map of category names to their percentage values
     */
    public void setPieChart(Map<String, Double> data) {
        this.categoryData = data;
    }

    /**
     * Displays category insights with percentage breakdowns.
     * <p>
     * Clears and rebuilds the category container with rows for each category,
     * showing emoji icons, category names, and percentage values.
     * </p>
     */
    public void showCategoryInsights() {
        if (categoryContainer == null || categoryData == null) return;

        categoryContainer.getChildren().clear();
        if (categoryData.isEmpty()) return;

        categoryData.forEach((category, percentage) -> {
            HBox row = buildCategoryRow(category, percentage);
            categoryContainer.getChildren().add(row);
        });
    }

    /**
     * Builds a row for a single category in the insights panel.
     *
     * @param category the category name
     * @param percentage the percentage value for this category
     * @return an HBox containing the category row UI
     */
    private HBox buildCategoryRow(String category, double percentage) {
        HBox row = new HBox(12);
        row.setStyle(
                "-fx-background-color: rgba(255,255,255,0.03);" +
                        "-fx-padding: 10 12; -fx-background-radius: 12;"
        );

        String emoji = getCategoryEmoji(category);
        Label icon = new Label(emoji);
        icon.setStyle(
                "-fx-background-color: rgba(16,185,129,0.1);" +
                        "-fx-padding: 8; -fx-background-radius: 10;"
        );

        Label nameLabel = new Label(category);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13;");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Label percentLabel = new Label(String.format("%.1f%%", percentage));
        percentLabel.setStyle("-fx-text-fill: #10b981; -fx-font-size: 12; -fx-font-weight: bold;");

        row.getChildren().addAll(icon, nameLabel, percentLabel);
        return row;
    }

    /**
     * Returns an emoji icon for a given category name.
     *
     * @param category the category name
     * @return the corresponding emoji character, or a default money bag emoji
     */
    private String getCategoryEmoji(String category) {
        if (category == null) return "\ud83d\udcb0";
        switch (category.toLowerCase()) {
            case "food": return "\ud83c\udf54";
            case "transport": return "\ud83d\ude97";
            case "entertainment": return "\ud83c\udfae";
            case "shopping": return "\ud83d\uded2";
            case "education": return "\ud83d\udcda";
            case "health": return "\ud83d\udc8a";
            default: return "\ud83d\udcb0";
        }
    }

    /**
     * Displays an alert message (currently prints to console).
     *
     * @param message the alert message to display
     */
    public void showAlert(String message) {
        System.out.println("ALERT: " + message);
    }

    /**
     * Shows a warning for the final day of the budget cycle.
     * <p>
     * Changes the daily limit text color to orange.
     * </p>
     */
    public void showFinalDayWarning() {
        tvDailyLimit.setStyle("-fx-text-fill: #f59e0b;");
    }

    /**
     * Shows a message when no data is available.
     */
    public void showNoDataMessage() {
        tvDailyLimit.setText("No Data");
    }

    /**
     * Displays an alert banner when the daily limit is exceeded.
     * <p>
     * Creates and inserts a red-themed alert banner at the top of the
     * daily limit card with a warning message.
     * </p>
     */
    public void showLimitExceededAlert() {
        if (alertBanner == null && dailyLimitCard != null) {
            alertBanner = new VBox(5);
            alertBanner.setStyle(
                    "-fx-background-color: rgba(239, 68, 68, 0.15);" +
                            "-fx-background-radius: 10;" +
                            "-fx-border-color: rgba(239, 68, 68, 0.4);" +
                            "-fx-border-radius: 10;" +
                            "-fx-padding: 8 12;"
            );

            Label alertIcon = new Label("\u26a0\ufe0f");
            alertIcon.setStyle("-fx-font-size: 12;");

            Label alertText = new Label("Daily Limit Exceeded! Your next daily limit will be reduced.");
            alertText.setStyle(
                    "-fx-text-fill: #fca5a5;" +
                            "-fx-font-size: 10;" +
                            "-fx-font-weight: bold;" +
                            "-fx-wrap-text: true;"
            );

            alertBanner.getChildren().addAll(alertIcon, alertText);
            dailyLimitCard.getChildren().add(0, alertBanner);
        }
    }

    /**
     * Changes the daily limit text color based on spending status.
     *
     * @param isOverspent {@code true} for red color, {@code false} for green
     */
    public void showLimitColor(boolean isOverspent) {
        if (isOverspent) {
            tvDailyLimit.setStyle("-fx-text-fill: #ef4444;");
        } else {
            tvDailyLimit.setStyle("-fx-text-fill: #10b981;");
        }
    }

    /**
     * Hides the limit exceeded alert banner if it is currently displayed.
     */
    public void hideLimitExceededAlert() {
        if (alertBanner != null && dailyLimitCard != null) {
            dailyLimitCard.getChildren().remove(alertBanner);
            alertBanner = null;
        }
    }

    /**
     * Event handler for the Log Expense button click.
     * <p>
     * Navigates to the Quick Entry screen when triggered.
     * </p>
     */
    @FXML
    public void onLogExpenseClicked() {
        if (onNavigateToQuickEntry != null) {
            onNavigateToQuickEntry.run();
        }
    }

    /**
     * Event handler for the History button click.
     * <p>
     * Navigates to the History screen when triggered.
     * </p>
     */
    @FXML
    public void onHistoryClicked() {
        if (onNavigateToHistory != null) {
            onNavigateToHistory.run();
        }
    }
}