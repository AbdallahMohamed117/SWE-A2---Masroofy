package com.example.masroofy.Controller;

import com.example.masroofy.Listener.QuickEntryListener;
import com.example.masroofy.Model.Entity.Transaction;
import com.example.masroofy.Model.History;
import com.example.masroofy.View.QuickEntryView;

public class QuickEntryEditController implements QuickEntryListener {
    private History model;
    private QuickEntryView view;
    private Runnable onDone;

    public QuickEntryEditController(History model, QuickEntryView view, Transaction transaction, Runnable onDone) {
        this.model = model;
        this.view = view;
        this.onDone = onDone;
        view.setListener(this);
        view.enterEditMode(transaction);
        view.showCategories(model.getCategories());
    }

    @Override
    public void onSubmitExpense(Transaction transaction) {
    }

    @Override
    public void onEditSubmitted(Transaction transaction) {
        boolean success = model.editTransaction(transaction);
        if (success) {
            view.showUpdateConfirmation();
            if (onDone != null) onDone.run();
        } else {
            view.showErrorMessage("New amount exceeds your remaining allowance!");
        }
    }

    @Override
    public void onAddCategoryClicked(String category) {
    }

    @Override
    public boolean onBackClicked() {
        if (onDone != null) onDone.run();
        return true;
    }
}
