package com.example.masroofy.Listener;

import com.example.masroofy.Model.Entity.Transaction;

public interface QuickEntryListener {
    void onSubmitExpense(String amountText, String category);
    void onEditSubmitted(Transaction originalTransaction, String amountText, String category);
    void onAddCategoryClicked(String category);
    boolean onBackClicked();
}
