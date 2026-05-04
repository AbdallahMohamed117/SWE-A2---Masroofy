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
        view.showCategories(model.getCategories());
    }



    @Override
    public void onSubmitExpense(Transaction transaction) {
        model.addTransaction(transaction);
    }

    @Override
    public void onAddCategoryClicked(String category) {
        Category newCategory = new Category();
        newCategory.setCategoryName(category);
        model.addCategory(newCategory);
    }
}