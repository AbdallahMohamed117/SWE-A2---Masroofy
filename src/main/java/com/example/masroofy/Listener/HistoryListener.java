package com.example.masroofy.Listener;

import com.example.masroofy.Model.Entity.Transaction;

import java.util.Date;

public interface HistoryListener {
    public void onCategoryFilerClicked();
    public void onDateFilterClicked();
    public void onApplyFilterClicked();
    void onEditClicked(Transaction transaction);
    void onEditSubmitted(Transaction transaction);
    void onDeleteClicked(Transaction transaction);
    void onFilterApplied(String category, Date from, Date to);
}
