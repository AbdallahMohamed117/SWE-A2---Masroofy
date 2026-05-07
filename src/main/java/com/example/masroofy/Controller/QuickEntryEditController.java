package com.example.masroofy.Controller;

import com.example.masroofy.Listener.QuickEntryListener;
import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;
import com.example.masroofy.Model.History;
import com.example.masroofy.View.QuickEntryView;
import com.example.masroofy.util.QuickEntryValidator;

/**
 * Controller for editing existing transactions in the Masroofy application.
 * <p>
 * The {@code QuickEntryEditController} handles the modification of previously
 * saved transactions. It reuses the Quick Entry view but configures it in
 * edit mode, allowing users to update transaction amounts and categories.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Loading an existing transaction into the edit form</li>
 *   <li>Validating edited input (amount and category selection)</li>
 *   <li>Updating the transaction in the history model</li>
 *   <li>Navigating back to the history screen after successful update</li>
 *   <li>Checking allowance limits when updating transaction amounts</li>
 * </ul>
 *
 * <p><b>Edit Flow:</b></p>
 * <ol>
 *   <li>User selects a transaction to edit from the History screen</li>
 *   <li>This controller loads the transaction data into the view</li>
 *   <li>User modifies amount and/or category</li>
 *   <li>Controller validates input and updates the transaction</li>
 *   <li>View returns to History screen with updated data</li>
 * </ol>
 *
 * @version 1.0
 * @since 1.0
 * @see QuickEntryListener
 * @see History
 * @see QuickEntryView
 * @see QuickEntryValidator
 * @see QuickEntryController
 */
public class QuickEntryEditController implements QuickEntryListener {

    /** The history model for updating the existing transaction. */
    private History model;

    /** The Quick Entry view configured in edit mode. */
    private QuickEntryView view;

    /** Callback to execute after successful edit or back navigation. */
    private Runnable onDone;

    /** Utility class for validating expense input. */
    private QuickEntryValidator validator = new QuickEntryValidator();

    /**
     * Constructs a {@code QuickEntryEditController} for editing a transaction.
     * <p>
     * Initializes the controller by:
     * <ul>
     *   <li>Setting up model, view, and completion callback references</li>
     *   <li>Registering this controller as the view's listener</li>
     *   <li>Putting the view into edit mode with the existing transaction data</li>
     *   <li>Populating the category dropdown with available categories</li>
     * </ul>
     *
     * @param model the history model used to update the transaction
     * @param view the Quick Entry view (will be put into edit mode)
     * @param transaction the transaction to be edited
     * @param onDone callback to execute when editing is complete (e.g., return to History)
     * @throws NullPointerException if any parameter is {@code null}
     */
    public QuickEntryEditController(History model, QuickEntryView view, Transaction transaction, Runnable onDone) {
        if (model == null) {
            throw new NullPointerException("History model cannot be null");
        }
        if (view == null) {
            throw new NullPointerException("QuickEntry view cannot be null");
        }
        if (transaction == null) {
            throw new NullPointerException("Transaction cannot be null");
        }
        this.model = model;
        this.view = view;
        this.onDone = onDone;
        view.setListener(this);
        view.enterEditMode(transaction);
        view.showCategories(model.getCategories());
    }

    /**
     * Handles the submission of edited transaction data.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Validates the amount format and category selection</li>
     *   <li>Converts the amount text to a double (safe due to prior validation)</li>
     *   <li>Updates the original transaction with new amount and category</li>
     *   <li>Attempts to save the updated transaction to the model</li>
     *   <li>Displays success message and navigates back on success, or error message on failure</li>
     * </ol>
     *
     * <p><b>Error Conditions:</b></p>
     * <ul>
     *   <li>Invalid amount format or empty category → Validation error message</li>
     *   <li>Updated amount exceeds remaining allowance → Allowance error message</li>
     * </ul>
     *
     * @param originalTransaction the original transaction being edited
     * @param amountText the new amount as text (will be parsed to double)
     * @param category the new category name
     */
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

    /**
     * Handles back button navigation during edit mode.
     * <p>
     * When the user cancels editing, this method executes the completion callback
     * to return to the previous screen (typically the History screen) without
     * saving any changes.
     * </p>
     *
     * @return {@code true} always, indicating back navigation is allowed
     */
    @Override
    public boolean onBackClicked() {
        if (onDone != null) onDone.run();
        return true;
    }

    /**
     * Not implemented for edit mode.
     * <p>
     * Category addition is handled by {@link QuickEntryController},
     * not by the edit controller.
     * </p>
     *
     * @param category the category name to add (unused)
     */
    @Override
    public void onAddCategoryClicked(String category) {
        // Category addition is disabled in edit mode
    }

    /**
     * Not implemented for edit mode.
     * <p>
     * Expense submission in edit mode is handled by {@link #onEditSubmitted}
     * instead of this method.
     * </p>
     *
     * @param amountText the expense amount as text (unused)
     * @param category the expense category (unused)
     */
    @Override
    public void onSubmitExpense(String amountText, String category) {
        // Expense submission is handled by onEditSubmitted in edit mode
    }
}