package com.example.masroofy.Listener;

import com.example.masroofy.Model.Entity.Transaction;

import java.util.Date;

public interface HistoryListener {
    void onCategoryFilterClicked(String category);
    void onDateFilterClicked(Date date);
    void onFilterApplied(String category, Date fromDate, Date toDate);
    void onEditClicked(Transaction transaction);
    void onEditSubmitted(Transaction transaction);
    void onDeleteClicked(Transaction transaction);
}
