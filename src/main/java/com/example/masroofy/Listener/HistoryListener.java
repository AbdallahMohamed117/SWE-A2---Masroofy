package com.example.masroofy.Listener;

import com.example.masroofy.Model.Entity.Transaction;

import java.util.Date;

/**
 * Listener interface for handling user interactions on the History screen.
 * <p>
 * The {@code HistoryListener} interface defines callback methods for all user
 * actions that can occur on the Transaction History screen. Implementations
 * (typically the {@code HistoryController}) respond to these events by updating
 * the model and refreshing the view.
 * </p>
 *
 * <p><b>Available Actions:</b></p>
 * <ul>
 *   <li>Applying filters (category and date range)</li>
 *   <li>Clearing all active filters</li>
 *   <li>Editing a transaction</li>
 *   <li>Deleting a transaction</li>
 *   <li>Navigating back to the previous screen</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see com.example.masroofy.Controller.HistoryController
 * @see com.example.masroofy.View.HistoryView
 * @see Transaction
 */
public interface HistoryListener {

    /**
     * Called when a filter is applied to the transaction list.
     * <p>
     * This method is triggered when the user selects a category and/or date range
     * to filter the displayed transactions. The implementation should retrieve
     * only the transactions matching the specified criteria.
     * </p>
     *
     * @param category the selected category for filtering, or {@code null} to include all categories
     * @param fromDate the start date for filtering, or {@code null} for no lower bound
     * @param toDate the end date for filtering, or {@code null} for no upper bound
     */
    void onFilterApplied(String category, Date fromDate, Date toDate);

    /**
     * Called when all filters are cleared.
     * <p>
     * This method is triggered when the user chooses to remove all applied filters.
     * The implementation should display all transactions without any filtering.
     * </p>
     */
    void onFilterCleared();

    /**
     * Called when the edit button is clicked on a transaction.
     * <p>
     * This method is triggered when the user initiates editing of a specific
     * transaction. The implementation should navigate to an edit screen
     * (typically Quick Entry in edit mode) with the selected transaction loaded.
     * </p>
     *
     * @param transaction the transaction to be edited
     */
    void onEditClicked(Transaction transaction);

    /**
     * Called when an edit operation is submitted.
     * <p>
     * This method is triggered when the user confirms changes to an edited
     * transaction. The implementation should save the updated transaction data
     * to the model and refresh the history view.
     * </p>
     *
     * @param transaction the transaction with updated data
     */
    void onEditSubmitted(Transaction transaction);

    /**
     * Called when the delete button is clicked on a transaction.
     * <p>
     * This method is triggered when the user requests deletion of a specific
     * transaction. The implementation should remove the transaction from the
     * model and refresh the history view.
     * </p>
     *
     * @param transaction the transaction to be deleted
     */
    void onDeleteClicked(Transaction transaction);

    /**
     * Called when the back button is clicked.
     * <p>
     * This method is triggered when the user wants to navigate back to the
     * previous screen (typically the Dashboard). The return value determines
     * whether the navigation should proceed.
     * </p>
     *
     * @return {@code true} if navigation back is allowed, {@code false} to cancel
     */
    boolean onBackClicked();
}