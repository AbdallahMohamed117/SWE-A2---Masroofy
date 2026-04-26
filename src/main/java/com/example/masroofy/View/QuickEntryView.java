package com.example.masroofy.View;

import com.example.masroofy.Model.Entity.*;
import java.util.List;

public class QuickEntryView implements AbstractView {
    private double amount;
    private String selectedCategory;

    @Override
    public void printScreen() {}

    public void showCategories(List<String> categories) {}

    public void onExpenseSubmitted(double amount, String category) {}

    public void showErrorMessage(String msg) {}

    public void showSavedConfirmation() {}
}