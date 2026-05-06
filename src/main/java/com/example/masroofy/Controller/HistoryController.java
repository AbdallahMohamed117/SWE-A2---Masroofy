package com.example.masroofy.Controller;

import com.example.masroofy.Listener.HistoryListener;
import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.Transaction;
import com.example.masroofy.View.*;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.Arrays;
import java.util.Date;

public class HistoryController implements AbstractController, HistoryListener {
    private History model;
    private HistoryView view;

    public HistoryController(History m, HistoryView v) {
        model = m;
        view = v;
        view.setListener(this);
        view.setComboBox(model.getCategories());
        view.setDateFilters(Arrays.asList("Last Day", "Last Week", "Last Month"));
        PrintView();
    }

    @Override
    public void PrintView() {
        view.showTransactions(model.getTransactions());


    }

    @Override
    public void onFilterApplied(String category, Date fromDate, Date toDate) {
        String selectedCategory = category != null ? category : null;
        Date start = fromDate != null ? fromDate : null;
        Date end = toDate != null ? toDate : null;

        view.showTransactions(model.getTransactions(selectedCategory, start, end));
    }

    @Override
    public void onFilterCleared() {
        view.showTransactions(model.getTransactions());
    }

    @Override
    public void onEditClicked(Transaction transaction) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/masroofy/View/QuickEntry.fxml"));
            Parent quickEntryRoot = loader.load();
            QuickEntryView qeView = loader.getController();

            qeView.setOnNavigateBack(() -> {
                PrintView();
            });

            Runnable onDone = () -> {
                qeView.setOnNavigateBack(null);
                PrintView();
            };

            new QuickEntryEditController(model, qeView, transaction, onDone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEditSubmitted(Transaction transaction) {
    }

    @Override
    public void onDeleteClicked(Transaction transaction) {
        model.deleteTransaction(transaction);
        PrintView();
    }

    @Override
    public boolean onBackClicked() {
        return true;
    }
}