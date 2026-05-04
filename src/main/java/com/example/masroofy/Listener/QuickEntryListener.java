package com.example.masroofy.Listener;

import com.example.masroofy.Model.Entity.Transaction;

public interface QuickEntryListener {
    void onSubmitExpense(Transaction transaction);
    void onAddCategoryClicked(String category);
}
