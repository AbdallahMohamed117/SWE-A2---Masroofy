package com.example.masroofy.Controller;

import com.example.masroofy.Listener.QuickEntryListener;
import com.example.masroofy.Model.Entity.*;
import com.example.masroofy.Model.*;
import com.example.masroofy.View.*;
import com.example.masroofy.util.QuickEntryValidator;

/**
 * Controller for the Quick Entry screen in the Masroofy application.
 * <p>
 * The {@code QuickEntryController} handles the creation of new expense transactions
 * and category management. It provides a fast interface for users to record expenses
 * by selecting categories and entering amounts.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Validating expense input (amount and category selection)</li>
 *   <li>Creating and saving expense transactions</li>
 *   <li>Managing expense categories (adding new categories)</li>
 *   <li>Displaying available categories from the model</li>
 *   <li>Checking allowance limits before saving transactions</li>
 * </ul>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Input validation before processing transactions</li>
 *   <li>Allowance limit checking to prevent overspending</li>
 *   <li>Dynamic category management (add new categories on the fly)</li>
 *   <li>Success confirmation messages for saved transactions</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractController
 * @see QuickEntryListener
 * @see QuickEntry
 * @see QuickEntryView
 * @see QuickEntryValidator
 */
public class QuickEntryController implements AbstractController, QuickEntryListener {

    /** The view for Quick Entry UI components and user interaction. */
    private QuickEntryView view;

    /** The model handling transaction storage and category management. */
    private QuickEntry model;

    /** Utility class for validating expense input (amount and category). */
    private QuickEntryValidator validator = new QuickEntryValidator();

    /**
     * Constructs a {@code QuickEntryController} with the specified model and view.
     * <p>
     * Initializes the controller by setting up the model-view references and
     * registering this controller as the QuickEntry listener.
     * </p>
     *
     * @param m the QuickEntry model for transaction and category operations
     * @param v the QuickEntry view for UI display
     * @throws NullPointerException if {@code m} or {@code v} is {@code null}
     */
    public QuickEntryController(QuickEntry m, QuickEntryView v) {
        if (m == null) {
            throw new NullPointerException("QuickEntry model cannot be null");
        }
        if (v == null) {
            throw new NullPointerException("QuickEntry view cannot be null");
        }
        model = m;
        view = v;
        view.setListener(this);
    }

    /**
     * Displays the available categories in the view.
     * <p>
     * This method implements the {@link AbstractController} interface by
     * populating the view's category selector with the list of categories
     * retrieved from the model.
     * </p>
     */
    @Override
    public void printView() {
        view.showCategoriesFromCategoryList(model.getCategories());
    }

    /**
     * Refreshes the view by reloading the category list.
     * <p>
     * This method simply delegates to {@link #printView()} to keep the
     * category display up-to-date after changes.
     * </p>
     */
    public void refreshView() {
        printView();
    }

    /**
     * Handles expense submission from the user.
     * <p>
     * This method performs the following steps:
     * <ol>
     *   <li>Validates the amount format and category selection</li>
     *   <li>Converts the amount text to a double (safe due to prior validation)</li>
     *   <li>Creates a {@link Category} and {@link Transaction} object</li>
     *   <li>Attempts to add the transaction to the model</li>
     *   <li>Displays appropriate success or error messages</li>
     * </ol>
     * </p>
     *
     * <p><b>Error Conditions:</b></p>
     * <ul>
     *   <li>Invalid amount format or empty category → Validation error message</li>
     *   <li>Transaction exceeds remaining allowance → Allowance error message</li>
     * </ul>
     *
     * @param amountText the expense amount as text (will be parsed to double)
     * @param category the selected expense category
     */
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

    /**
     * Handles edit submission for an existing transaction.
     * <p>
     * <b>Note:</b> This method is currently not implemented. Edit functionality
     * is handled by {@link QuickEntryEditController} instead.
     * </p>
     *
     * @param transaction the transaction being edited
     * @param amountText the new amount as text
     * @param category the new category
     */
    @Override
    public void onEditSubmitted(Transaction transaction, String amountText, String category) {
        // Edit functionality is handled by QuickEntryEditController
    }

    /**
     * Handles adding a new expense category.
     * <p>
     * This method validates the category name, creates a new {@link Category}
     * object, and attempts to add it to the model. If successful, the category
     * list in the view is refreshed and the new category input field is cleared.
     * </p>
     *
     * <p><b>Validation Rules:</b></p>
     * <ul>
     *   <li>Category name cannot be {@code null} or empty</li>
     *   <li>Category name must be unique (no duplicates)</li>
     * </ul>
     *
     * @param categoryName the name of the new category to add
     */
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

    /**
     * Handles back button navigation events.
     * <p>
     * This method always returns {@code true} to indicate that navigation back
     * to the previous screen is permitted.
     * </p>
     *
     * @return {@code true} always, indicating back navigation is allowed
     */
    @Override
    public boolean onBackClicked() {
        return true;
    }
}