package com.example.masroofy.Controller;

import com.example.masroofy.Listener.DashboardListener;
import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.*;
import com.example.masroofy.View.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController implements AbstractController, DashboardListener {

    private Dashboard model;
    private DashboardView view;
    private History historyModel;

    public DashboardController(Dashboard m, DashboardView v) {
        view = v;
        model = m;
        historyModel = new History();
        view.setListener(this);
        PrintView();
    }
    @Override
    public void PrintView() {
        refreshDashboard();
    }

    public void refreshDashboard() {
        Map<String, Double> pieChart = new HashMap<>();
        double progress = 0;
        boolean isOverspent = false;
        double totalSpent = 0;


        view.setDaysLeft(model.getDaysLeft());
        model.recalculateDailyLimitIfNewDay();

        double dailyLimit = model.getDailyLimit();

        view.setDailyLimit(dailyLimit);
        view.setDaysLeft(model.getDaysLeft());

        List<Transaction> amounts = historyModel.getTransactions();
        double allowance = model.getAllowance();
        if(!amounts.isEmpty()) {
            for (Transaction t : amounts) {
                totalSpent += t.getTransactionAmount();
                Category category = t.getTransactionCategory();
                String categoryName = category.getCategoryName();

                if (allowance > 0) {
                    double percentage = (t.getTransactionAmount() / allowance) * 100;
                    pieChart.merge(categoryName, percentage, Double::sum);
                } else {
                    pieChart.merge(categoryName, 0.0,Double::sum);
                }
            }

             progress = totalSpent / allowance;
             isOverspent = totalSpent > dailyLimit;
        }
        
        view.setTotalSpent(totalSpent);
        view.setPieChart(pieChart);
        view.setProgressBar(progress);
        view.updatePieChartProgress(progress);
        view.setPieChartTotal(totalSpent);
        view.showLimitColor(isOverspent);
        view.setStatusIcon(isOverspent);

        if (model.isDailyLimitExceeded()) {
            view.showLimitExceededAlert();
        } else {
            view.hideLimitExceededAlert();
        }

        view.printScreen();
    }

    @Override
    public void onLogExpenseClicked() {
    }

    @Override
    public void onHistoryClicked() {
    }
}