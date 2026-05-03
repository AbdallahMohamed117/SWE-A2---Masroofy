package com.example.masroofy.Listener;

import com.example.masroofy.Model.Entity.Transaction;

import java.util.Date;

public interface HistoryListener {
    public void onCategoryFilerClicked(String category);
    public void onDateFilterClicked(Date date);
    public void onApplyFilterClicked();
    void onEditClicked(Transaction transaction);
    void onEditSubmitted(Transaction transaction);
    void onDeleteClicked(Transaction transaction);
}
