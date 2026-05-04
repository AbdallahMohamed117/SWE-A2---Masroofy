package com.example.masroofy.Controller;

import com.example.masroofy.Listener.DashboardListener;
import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.*;
import com.example.masroofy.View.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController implements AbstractController, DashboardListener {

    public DashboardController(Dashboard m, DashboardView v) {
        view = v;
        model = m;
        view.setListener(this);
        PrintView();
    }
    @Override
    public void PrintView() {
        refreshDashboard();
    }

    public void refreshDashboard() {
        double limit = 60;

        if (limit <= 0) {
            view.showNoDataMessage();
            view.setDaysLeft(model.getDaysLeft());
            return;
        }

        List<Transaction> amounts = model.getPiechartData();

        double totalSpent = 0;
        Map<String, Double> piechart = new HashMap<>();
        for(Transaction t : amounts) {
            totalSpent += t.getTransactionAmount();
            double percentage = (t.getTransactionAmount() / limit) * 100;
            Category category = t.getTransactionCategory();
            String categoryName = category.getCategoryName();

            piechart.put(categoryName, percentage);
        }

        double progress = totalSpent / limit;
        boolean isOverspent = totalSpent > limit;

        view.setDailyLimit(limit);
        view.setTotalSpent(totalSpent);
        view.setPieChart(piechart);
        view.setProgressBar(progress);
        view.updatePieChartProgress(progress);
        view.setPieChartTotal(totalSpent);
        view.showLimitColor(isOverspent);
        view.setStatusIcon(isOverspent);
        view.setDaysLeft(model.getDaysLeft());

        view.printScreen();
    }

    private Dashboard model;
    private DashboardView view;

    @Override
    public void onLogExpenseClicked() {
    }

    @Override
    public void onHistoryClicked() {
    }
}