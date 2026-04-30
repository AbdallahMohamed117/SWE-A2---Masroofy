package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardView implements AbstractView, Initializable {

    @FXML private Label tvDailyLimit;
    @FXML private Button btnLogExpense;
    @FXML private Button btnHistory;
    @FXML private ProgressBar progressBar;
    @FXML private VBox categoryContainer;

    private double dailyLimit;
    private Map<String, Double> categoryData;

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
    }

    public void setDailyLimit(double limit) {
        this.dailyLimit = limit;
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

        categoryData.forEach((category, amount) -> {
            System.out.println(category + ": EGP " + String.format("%.2f", amount));
        });
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

    @FXML public void onLogExpenseClicked() { }
    @FXML public void onHistoryClicked() { }
}
