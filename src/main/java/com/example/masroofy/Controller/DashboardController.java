package com.example.masroofy.Controller;

import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.*;
import com.example.masroofy.View.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController implements AbstractController {

    public DashboardController(DashboardView v, Dashboard m) {
        view = v;
        model = m;
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
}