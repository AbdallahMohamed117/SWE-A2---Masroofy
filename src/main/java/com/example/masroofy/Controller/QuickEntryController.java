package com.example.masroofy.Controller;

import com.example.masroofy.Listener.QuickEntryListener;
import com.example.masroofy.Model.Entity.*;
import com.example.masroofy.Model.*;
import com.example.masroofy.View.*;
import com.example.masroofy.util.QuickEntryValidator;

public class QuickEntryController implements AbstractController, QuickEntryListener{
    private QuickEntryView view;
    private QuickEntry model;
    private QuickEntryValidator validator = new QuickEntryValidator();

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
    public void onSubmitExpense(String amountText, String category) {
        if (!validator.validate(amountText, category)) {
            view.showErrorMessage("Invalid input. Please check amount and category.");
            return;
        }

        double amount = Double.parseDouble(amountText); // Safe - already validated

        Category c = new Category(category);
        Transaction t = new Transaction(amount, c, System.currentTimeMillis());

        boolean success = model.addTransaction(t);
        if (success) {
            view.showSavedConfirmation();
            view.printScreen();
            view.showCategoriesFromCategoryList(model.getCategories());
        } else {
            view.showErrorMessage("Transaction amount exceeds your remaining allowance!");
        }
    }

    @Override
    public void onEditSubmitted(Transaction transaction, String amountText, String category) {

    }

    @Override
    public void onAddCategoryClicked(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            view.showErrorMessage("Category name cannot be empty!");
            return;
        }

        Category newCategory = new Category(categoryName);

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