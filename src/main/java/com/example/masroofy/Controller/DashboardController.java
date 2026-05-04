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
    }
    @Override
    public void PrintView() {
        view.printScreen();
    }

    public void refreshDashboard() {
        double limit = model.getDailyLimit();
        List<Transaction> amounts = model.getPiechartData();

        Map<String, Double> piechart = new HashMap<>();
        for(Transaction t : amounts) {
            double percentage = (t.getTransactionAmount() / limit) * 100;
            Category category = t.getTransactionCategory();
            String categoryName = category.getCategoryName();

            piechart.put(categoryName,percentage);
        }

        view.setDailyLimit(limit);
        view.setPieChart(piechart);

        PrintView();
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