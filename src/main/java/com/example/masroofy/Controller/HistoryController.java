package com.example.masroofy.Controller;

import com.example.masroofy.Listener.HistoryListener;
import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.Transaction;
import com.example.masroofy.View.*;

import java.util.Arrays;
import java.util.Date;

/**
 * Controller for the Transaction History screen in the Masroofy application.
 * <p>
 * The {@code HistoryController} manages the display and manipulation of transaction
 * history. It implements both {@link AbstractController} for standard view refresh
 * operations and {@link HistoryListener} to respond to user interactions such as
 * filtering, editing, and deleting transactions.
 * </p>
 *
 * <p><b>Key Responsibilities:</b></p>
 * <ul>
 *   <li>Displaying all transactions with optional category and date filters</li>
 *   <li>Handling filter application and clearing operations</li>
 *   <li>Managing transaction deletion from the history</li>
 *   <li>Refreshing the view when data changes</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractController
 * @see HistoryListener
 * @see History
 * @see HistoryView
 */
public class HistoryController implements AbstractController, HistoryListener {

    /** The history model providing transaction data and operations. */
    private History model;

    /** The history view for displaying transactions and handling UI events. */
    private HistoryView view;

    /**
     * Constructs a {@code HistoryController} with the specified model and view.
     * <p>
     * Initializes the controller by:
     * <ul>
     *   <li>Setting the view's event listener to this controller</li>
     *   <li>Populating the category combo box with available categories</li>
     *   <li>Setting predefined date filter options (Last Day, Last Week, Last Month)</li>
     *   <li>Refreshing the history display</li>
     * </ul>
     * </p>
     *
     * @param m the history model containing transaction data
     * @param v the history view for UI display
     * @throws NullPointerException if {@code m} or {@code v} is {@code null}
     */
    public HistoryController(History m, HistoryView v) {
        if (m == null) {
            throw new NullPointerException("History model cannot be null");
        }
        if (v == null) {
            throw new NullPointerException("History view cannot be null");
        }
        model = m;
        view = v;
        view.setListener(this);
        view.setComboBox(model.getCategories());
        view.setDateFilters(Arrays.asList("Last Day", "Last Week", "Last Month"));
        printView();
    }

    /**
     * Refreshes the history view by calling {@link #refreshHistory()}.
     * <p>
     * This method implements the {@link AbstractController} interface requirement.
     * </p>
     */
    @Override
    public void printView() {
        refreshHistory();
    }

    /**
     * Refreshes the transaction history display.
     * <p>
     * This method updates the category combo box with the latest categories
     * and refreshes the transaction list to show all transactions.
     * </p>
     */
    public void refreshHistory() {
        view.setComboBox(model.getCategories());
        view.showTransactions(model.getTransactions());
    }

    /**
     * Handles filter application events from the view.
     * <p>
     * When a filter is applied, this method retrieves transactions matching the
     * specified category and/or date range, then updates the view to display
     * only the filtered results.
     * </p>
     * <p>
     * <b>Note:</b> Currently, the date range parameters are not being used in the
     * filter implementation. Future enhancements should implement date-based filtering
     * using the {@code fromDate} and {@code toDate} parameters.
     * </p>
     *
     * @param category the selected category for filtering (nullable for all categories)
     * @param fromDate the start date for filtering (currently unused)
     * @param toDate the end date for filtering (currently unused)
     */
    @Override
    public void onFilterApplied(String category, Date fromDate, Date toDate) {
        String selectedCategory = category != null ? category : null;
        Date start = fromDate != null ? fromDate : null;
        Date end = toDate != null ? toDate : null;

        view.showTransactions(model.getTransactions(selectedCategory, start, end));
    }

    /**
     * Handles filter clearing events from the view.
     * <p>
     * When filters are cleared, this method displays all transactions
     * without any filtering applied.
     * </p>
     */
    @Override
    public void onFilterCleared() {
        view.showTransactions(model.getTransactions());
    }

    /**
     * Handles edit button clicks for a transaction.
     * <p>
     * <b>Note:</b> This method is currently not implemented. Edit functionality
     * should be implemented to navigate to the Quick Entry screen in edit mode.
     * </p>
     *
     * @param transaction the transaction to be edited
     */
    @Override
    public void onEditClicked(Transaction transaction) {
        // TODO: Implement edit navigation
    }

    /**
     * Handles edit submission for a transaction.
     * <p>
     * <b>Note:</b> This method is currently not implemented. It would typically
     * be called after editing changes are submitted.
     * </p>
     *
     * @param transaction the edited transaction
     */
    @Override
    public void onEditSubmitted(Transaction transaction) {
        // TODO: Implement edit submission logic
    }

    /**
     * Handles delete button clicks for a transaction.
     * <p>
     * This method deletes the specified transaction from the model and
     * refreshes the view to reflect the updated transaction list.
     * </p>
     *
     * @param transaction the transaction to be deleted
     */
    @Override
    public void onDeleteClicked(Transaction transaction) {
        model.deleteTransaction(transaction);
        printView();
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