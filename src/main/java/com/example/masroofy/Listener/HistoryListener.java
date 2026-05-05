package com.example.masroofy.Listener;

import com.example.masroofy.Model.Entity.Transaction;

import java.util.Date;

public interface HistoryListener {
    void onFilterApplied(String category, Date fromDate, Date toDate);
    void onFilterCleared();
    void onEditClicked(Transaction transaction);
    void onEditSubmitted(Transaction transaction);
    void onDeleteClicked(Transaction transaction);
    boolean onBackClicked();
}
