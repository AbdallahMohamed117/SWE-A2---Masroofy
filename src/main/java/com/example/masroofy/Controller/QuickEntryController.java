package com.example.masroofy.Controller;

import com.example.masroofy.Listener.QuickEntryListener;
import com.example.masroofy.Model.Entity.*;
import com.example.masroofy.Model.*;
import com.example.masroofy.View.*;

public class QuickEntryController implements AbstractController, QuickEntryListener {
    private QuickEntryView view;
    private QuickEntry model;

    public QuickEntryController(QuickEntry m, QuickEntryView v) {
        model = m;
        view = v;
        view.setListener(this);
    }
    @Override
    public void PrintView() {
        view.showCategoriesFromCategoryList(model.getCategories());
    }



    @Override
    public void onSubmitExpense(Transaction transaction) {
        boolean success = model.addTransaction(transaction);
        if (success) {
            view.showSavedConfirmation();
            view.printScreen();
        } else {
            view.showErrorMessage("Transaction amount exceeds your remaining allowance!");
        }
    }

    @Override
    public void onEditSubmitted(Transaction transaction) {
    }

    @Override
    public void onAddCategoryClicked(String category) {
        if (category == null || category.trim().isEmpty()) {
            view.showErrorMessage("Category name cannot be empty!");
            return;
        }
        Category newCategory = new Category();
        newCategory.setCategoryName(category);
        boolean success = model.addCategory(newCategory);
        if (success) {
            view.clearNewCategory();
            view.showCategoriesFromCategoryList(model.getCategories());
        } else {
            view.showErrorMessage("Category already exists!");
        }
    }

    @Override
    public boolean onBackClicked() {
        return true;
    }
}