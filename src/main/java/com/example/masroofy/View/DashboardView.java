package com.example.masroofy.View;

import java.util.Map;

public class DashboardView implements AbstractView {
    private double dailyLimit;
    private Map<String, Double> categoryData;

    @Override
    public void printScreen() {}

    public void showSafeDailyLimit(double limit) {}

    public void showPieChart(Map<String, Double> data) {}

    public void showAlert(String message) {}

    public void showFinalDayWarning() {}

    public void showLimitColor(boolean isOverspent) {}

    public void showNoDataMessage() {}
}