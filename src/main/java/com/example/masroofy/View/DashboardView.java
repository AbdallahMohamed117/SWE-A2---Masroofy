package com.example.masroofy.View;

import com.example.masroofy.Listener.DashboardListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardView implements AbstractView, Initializable {

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

    private DashboardListener listener;
    private double dailyLimit;
    private double totalSpent;
    private Map<String, Double> categoryData;

    public void setListener(DashboardListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // runs when screen loads
    }

    @Override
    public void printScreen() {
        showSafeDailyLimit();
        showPieChart();
        showCategoryInsights();
    }

    public void showSafeDailyLimit() {
        tvDailyLimit.setText("EGP " + String.format("%.2f", dailyLimit));
        lblSpent.setText("Spent: EGP " + String.format("%.2f", totalSpent));
        lblLimit.setText("Limit: EGP " + String.format("%.2f", dailyLimit));
    }

    public void setDailyLimit(double limit) {
        this.dailyLimit = limit;
    }

    public void setTotalSpent(double spent) {
        this.totalSpent = spent;
    }

    public void setProgressBar(double progress) {
        if (progressBar != null) {
            progressBar.setProgress(Math.min(progress, 1.0));
        }
    }

    public void setDaysLeft(int days) {
        if (lblDaysLeft != null) {
            lblDaysLeft.setText(days + " days left");
        }
    }

    public void setStatusIcon(boolean isOverspent) {
        if (lblStatusIcon != null) {
            lblStatusIcon.setText(isOverspent ? "⚠️" : "✅");
        }
    }

    public void setPieChartTotal(double total) {
        if (lblPieChartTotal != null) {
            lblPieChartTotal.setText("EGP " + String.format("%.2f", total));
        }
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

    public void showPieChart() {
        // TODO: implement pie chart if needed
    }

    public void setPieChart(Map<String, Double> data) {
        this.categoryData = data;
    }

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

    private String getCategoryEmoji(String category) {
        if (category == null) return "💰";
        switch (category.toLowerCase()) {
            case "food": return "🍔";
            case "transport": return "🚗";
            case "entertainment": return "🎮";
            case "shopping": return "🛍️";
            case "education": return "📚";
            case "health": return "💊";
            default: return "💰";
        }
    }

    public void showAlert(String message) {
        System.out.println("ALERT: " + message);
    }

    public void showFinalDayWarning() {
        tvDailyLimit.setStyle("-fx-text-fill: #f59e0b;");
    }

    public void showLimitColor(boolean isOverspent) {
        if (isOverspent) {
            tvDailyLimit.setStyle("-fx-text-fill: #ef4444;");
        } else {
            tvDailyLimit.setStyle("-fx-text-fill: #10b981;");
        }
    }

    public void showNoDataMessage() {
        tvDailyLimit.setText("No Data");
    }

    @FXML public void onLogExpenseClicked() { if (listener != null) listener.onLogExpenseClicked(); }
    @FXML public void onHistoryClicked() { if (listener != null) listener.onHistoryClicked(); }
}
