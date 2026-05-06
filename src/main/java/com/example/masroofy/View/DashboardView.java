package com.example.masroofy.View;

import com.example.masroofy.Listener.DashboardListener;
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

public class DashboardView implements AbstractView {

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

    private VBox alertBanner;

    // ✅ أضف الـ fx:id الجديد
    @FXML private Label btnSettings;

    private DashboardListener listener;
    private Runnable onNavigateToQuickEntry;
    private Runnable onNavigateToHistory;
    private Runnable onNavigateToSettings; // ✅ جديد
    private double dailyLimit;
    private double totalSpent;
    private Map<String, Double> categoryData;

    public void setListener(DashboardListener listener) {
        this.listener = listener;
    }

    public void setOnNavigateToQuickEntry(Runnable r) { this.onNavigateToQuickEntry = r; }
    public void setOnNavigateToHistory(Runnable r) { this.onNavigateToHistory = r; }

    // ✅ الـ method الجديدة
    public void setOnNavigateToSettings(Runnable r) {
        this.onNavigateToSettings = r;
        if (btnSettings != null) {
            btnSettings.setOnMouseClicked(e -> onNavigateToSettings.run());
        }
    }

    @Override
    public void printScreen() {
        showSafeDailyLimit();
        showPieChart();
        showCategoryInsights();
    }

    public void setDaysLeft(int days) {
        if (lblDaysLeft != null) {
            lblDaysLeft.setText(days + " days left");
        }
    }

    public void setStatusIcon(boolean isOverspent) {
        if (lblStatusIcon != null) {
            lblStatusIcon.setText(isOverspent ? "\u26a0\ufe0f" : "\u2705");
        }
    }

    public void showSafeDailyLimit() {
        tvDailyLimit.setText("EGP " + String.format("%.2f", dailyLimit));
        lblSpent.setText("Spent: EGP " + String.format("%.2f", totalSpent));
        lblLimit.setText("Limit: EGP " + String.format("%.2f", dailyLimit));
    }

    public void setDailyLimit(double limit) { this.dailyLimit = limit; }
    public void setTotalSpent(double spent) { this.totalSpent = spent; }

    public void setProgressBar(double progress) {
        if (progressBar != null) progressBar.setProgress(Math.min(progress, 1.0));
    }

    public void setDaysLeft(int days) {
        if (lblDaysLeft != null) lblDaysLeft.setText(days + " days left");
    }

    public void setStatusIcon(boolean isOverspent) {
        if (lblStatusIcon != null)
            lblStatusIcon.setText(isOverspent ? "\u26a0\ufe0f" : "\u2705");
    }

    public void setPieChartTotal(double total) {
        if (lblPieChartTotal != null)
            lblPieChartTotal.setText("EGP " + String.format("%.2f", total));
    }

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

    public void showPieChart() {}

    public void setPieChart(Map<String, Double> data) { this.categoryData = data; }

    public void showCategoryInsights() {
        if (categoryContainer == null || categoryData == null || categoryData.isEmpty()) return;

        categoryContainer.getChildren().clear();
        categoryData.forEach((category, percentage) -> {
            HBox row = buildCategoryRow(category, percentage);
            categoryContainer.getChildren().add(row);
        });
    }

    private HBox buildCategoryRow(String category, double percentage) {
        HBox row = new HBox(12);
        row.setStyle(
                "-fx-background-color: rgba(255,255,255,0.03);" +
                        "-fx-padding: 10 12; -fx-background-radius: 12;"
        );

        String emoji = getCategoryEmoji(category);
        Label icon = new Label(emoji);
        icon.setStyle("-fx-background-color: rgba(16,185,129,0.1); -fx-padding: 8; -fx-background-radius: 10;");

        Label nameLabel = new Label(category);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 13;");
        HBox.setHgrow(nameLabel, Priority.ALWAYS);

        Label percentLabel = new Label(String.format("%.1f%%", percentage));
        percentLabel.setStyle("-fx-text-fill: #10b981; -fx-font-size: 12; -fx-font-weight: bold;");

        row.getChildren().addAll(icon, nameLabel, percentLabel);
        return row;
    }

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

    public void showAlert(String message) { System.out.println("ALERT: " + message); }

    public void showFinalDayWarning() { tvDailyLimit.setStyle("-fx-text-fill: #f59e0b;"); }
    public void showAlert(String message) {
        System.out.println("ALERT: " + message);
    }
    public void showFinalDayWarning() {
        tvDailyLimit.setStyle("-fx-text-fill: #f59e0b;");
    }
    public void showNoDataMessage() {
        tvDailyLimit.setText("No Data");
    }

    public void showLimitExceededAlert() {
        if (alertBanner == null && dailyLimitCard != null)
        {
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



    public void showLimitColor(boolean isOverspent) {
        tvDailyLimit.setStyle(isOverspent ? "-fx-text-fill: #ef4444;" : "-fx-text-fill: #10b981;");
    }

    public void showNoDataMessage() { tvDailyLimit.setText("No Data"); }

    @FXML public void onLogExpenseClicked() { if (onNavigateToQuickEntry != null) onNavigateToQuickEntry.run(); }
    @FXML public void onHistoryClicked() { if (onNavigateToHistory != null) onNavigateToHistory.run(); }
}

    public void hideLimitExceededAlert() {
        if (alertBanner != null && dailyLimitCard != null) {
            dailyLimitCard.getChildren().remove(alertBanner);
            alertBanner = null;
        }
    }

    @FXML public void onLogExpenseClicked() {
        if (onNavigateToQuickEntry != null){
            onNavigateToQuickEntry.run();
        }
    }

    @FXML public void onHistoryClicked() {
        if (onNavigateToHistory != null){
            onNavigateToHistory.run();
        }
    }
}
