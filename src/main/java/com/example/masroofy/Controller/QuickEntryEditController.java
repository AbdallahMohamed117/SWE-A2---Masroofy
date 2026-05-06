package com.example.masroofy.Controller;

import com.example.masroofy.Listener.QuickEntryListener;
import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;
import com.example.masroofy.Model.History;
import com.example.masroofy.View.QuickEntryView;
import com.example.masroofy.util.QuickEntryValidator;

public class QuickEntryEditController implements QuickEntryListener {
    private History model;
    private QuickEntryView view;
    private Runnable onDone;
    private QuickEntryValidator validator = new QuickEntryValidator();

    public QuickEntryEditController(History model, QuickEntryView view, Transaction transaction, Runnable onDone) {
        this.model = model;
        this.view = view;
        this.onDone = onDone;
        view.setListener(this);
        view.enterEditMode(transaction);
        view.showCategories(model.getCategories());
    }


    @Override
    public void onEditSubmitted(Transaction originalTransaction, String amountText, String category) {
        if (!validator.validate(amountText, category)) {
            view.showErrorMessage("Invalid input. Please check amount and category.");
            return;
        }

        double amount = Double.parseDouble(amountText);

        originalTransaction.setTransactionAmount(amount);
        originalTransaction.setTransactionCategory(new Category(category));

        boolean success = model.editTransaction(originalTransaction);
        if (success) {
            view.showUpdateConfirmation();
            if (onDone != null) onDone.run();
        } else {
            view.showErrorMessage("New amount exceeds your remaining allowance!");
        }
    }

    @Override
    public boolean onBackClicked() {
        if (onDone != null) onDone.run();
        return true;
    }


    @Override
    public void onAddCategoryClicked(String category) {
    }
    @Override
    public void onSubmitExpense(String amountText, String category) {
    }

}
