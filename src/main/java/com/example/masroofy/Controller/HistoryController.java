package com.example.masroofy.Controller;

import com.example.masroofy.Listener.HistoryListener;
import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.Transaction;
import com.example.masroofy.View.*;

import java.util.Date;

public class HistoryController implements AbstractController, HistoryListener {
    private History model;
    private HistoryView view;

    public HistoryController(History m, HistoryView v) {
        model = m;
        view = v;
        view.setListener(this);
    }
    @Override
    public void PrintView() {
        view.showTransactions(model.getTransactions());
    }

    @Override
    public void onCategoryFilerClicked() {

    }

    @Override
    public void onDataFilterClicked() {

    }

    @Override
    public void onApplyFilterClicked() {

    }

    @Override
    public void onAddTransactionClicked() {

    }

    @Override
    public void onEditClicked(Transaction transaction) {

    }

    @Override
    public void onEditSubmitted(Transaction transaction) {

    }

    @Override
    public void onDeleteClicked(Transaction transaction) {

    }

    @Override
    public void onFilterApplied(String category, Date from, Date to) {

    }

}