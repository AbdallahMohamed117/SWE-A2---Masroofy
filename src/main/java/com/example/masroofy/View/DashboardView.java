package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardView implements AbstractView, Initializable {

    @FXML private Label tvDailyLimit;
    @FXML private Button btnLogExpense;
    @FXML private Button btnHistory;
    @FXML private ProgressBar progressBar;

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
    }

    public void showSafeDailyLimit() {
        tvDailyLimit.setText("EGP " + String.format("%.2f", dailyLimit));
    }

    public void setDailyLimit(double limit) {
        dailyLimit = limit;
    }

    public void showPieChart() {
        // TODO: update chart with real data
    }

    public void setPieChart(Map<String, Double> data) {
        categoryData = data;
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

    @FXML
    public void onLogExpenseClicked() {
        // TODO: navigate to QuickEntryView
    }

    @FXML
    public void onHistoryClicked() {
        // TODO: navigate to HistoryView
    }
}