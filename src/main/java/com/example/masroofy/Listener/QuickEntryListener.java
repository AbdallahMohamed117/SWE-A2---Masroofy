package com.example.masroofy.Listener;

import com.example.masroofy.Model.Entity.Transaction;

/**
 * Listener interface for handling user interactions on the Quick Entry screen.
 * <p>
 * The {@code QuickEntryListener} interface defines callback methods for all user
 * actions that can occur on the Quick Entry screen. Implementations handle both
 * adding new expenses and editing existing transactions.
 * </p>
 *
 * <p><b>Primary Use Cases:</b></p>
 * <ul>
 *   <li>
 *     <b>Adding a new expense:</b>
 *     User enters amount, selects/creates a category, and submits
 *   </li>
 *   <li>
 *     <b>Editing an existing transaction:</b>
 *     User modifies amount and/or category of a previously saved transaction
 *   </li>
 *   <li>
 *     <b>Managing categories:</b>
 *     User adds a new category for expense classification
 *   </li>
 *   <li>
 *     <b>Navigating back:</b>
 *     User cancels or returns to previous screen
 *   </li>
 * </ul>
 *
 * <p><b>Implementing Classes:</b></p>
 * <ul>
 *   <li>{@code QuickEntryController} - Handles new expense entry</li>
 *   <li>{@code QuickEntryEditController} - Handles transaction editing</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see com.example.masroofy.Controller.QuickEntryController
 * @see com.example.masroofy.Controller.QuickEntryEditController
 * @see com.example.masroofy.View.QuickEntryView
 * @see Transaction
 */
public interface QuickEntryListener {

    /**
     * Called when a new expense is submitted.
     * <p>
     * This method is triggered when the user enters an amount, selects a category,
     * and submits the form to add a new expense transaction.
     * </p>
     *
     * <p><b>Implementation Responsibilities:</b></p>
     * <ul>
     *   <li>Validate the amount format (numeric, positive, etc.)</li>
     *   <li>Validate that a category is selected</li>
     *   <li>Create a new {@link Transaction} object</li>
     *   <li>Check if the transaction exceeds the remaining allowance</li>
     *   <li>Save the transaction to the model</li>
     *   <li>Display success or error feedback to the user</li>
     *   <li>Refresh the view with updated category list if needed</li>
     * </ul>
     *
     * @param amountText the expense amount as text (to be parsed to double)
     * @param category the selected expense category name
     */
    void onSubmitExpense(String amountText, String category);

    /**
     * Called when an edited transaction is submitted.
     * <p>
     * This method is triggered when the user modifies an existing transaction
     * and submits the changes. This is typically used in edit mode rather than
     * add mode.
     * </p>
     *
     * <p><b>Implementation Responsibilities:</b></p>
     * <ul>
     *   <li>Validate the new amount format</li>
     *   <li>Validate the new category selection</li>
     *   <li>Update the original transaction with new values</li>
     *   <li>Check if the updated amount exceeds the remaining allowance</li>
     *   <li>Save the changes to the model</li>
     *   <li>Display success or error feedback</li>
     *   <li>Navigate back to the history screen on success</li>
     * </ul>
     *
     * @param originalTransaction the transaction being edited (with original values)
     * @param amountText the new amount as text (to be parsed to double)
     * @param category the new category name
     */
    void onEditSubmitted(Transaction originalTransaction, String amountText, String category);

    /**
     * Called when a new category is added.
     * <p>
     * This method is triggered when the user enters a new category name and
     * submits it to be added to the available categories list.
     * </p>
     *
     * <p><b>Implementation Responsibilities:</b></p>
     * <ul>
     *   <li>Validate that the category name is not empty</li>
     *   <li>Check that the category does not already exist</li>
     *   <li>Add the new category to the model</li>
     *   <li>Refresh the category dropdown in the view</li>
     *   <li>Display success or error feedback</li>
     *   <li>Clear the category input field on success</li>
     * </ul>
     *
     * @param category the name of the new category to add
     */
    void onAddCategoryClicked(String category);

    /**
     * Called when the back button is clicked.
     * <p>
     * This method is triggered when the user wants to navigate back to the
     * previous screen without saving changes. The return value determines
     * whether the navigation should proceed.
     * </p>
     *
     * <p><b>Behavior by Mode:</b></p>
     * <ul>
     *   <li><b>Add mode:</b> Returns to Dashboard</li>
     *   <li><b>Edit mode:</b> Returns to History (discarding changes)</li>
     * </ul>
     *
     * @return {@code true} if navigation back is allowed, {@code false} to cancel
     */
    boolean onBackClicked();
}