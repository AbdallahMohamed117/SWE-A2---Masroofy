package com.example.masroofy.View;

import com.example.masroofy.Model.Entity.*;
import java.util.Date;
import java.util.List;

public class HistoryView implements AbstractView {
    private List<Transaction> transactions;

    @Override
    public void printScreen() {}

    public void showTransactions(List<Object> list) {}

    public void applyFilter(String category, Date from, Date to) {}

    public void showEmptyState() {}

    public void showEditForm(Object expense) {}

    public void showDeleteConfirmation() {}

    public void showUpdateConfirmation() {}

    public void showNoFilterResults() {}
}