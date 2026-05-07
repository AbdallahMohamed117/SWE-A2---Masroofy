package com.example.masroofy.Model.Entity;

/**
 * Represents a financial transaction in the Masroofy application.
 * <p>
 * The {@code Transaction} class encapsulates a single financial entry,
 * whether it's an expense (money spent) or income (money received).
 * Each transaction includes an amount, a spending category, and a timestamp
 * indicating when it occurred.
 * </p>
 *
 * <p><b>Key Components:</b></p>
 * <ul>
 *   <li><b>Amount:</b> The monetary value of the transaction (positive for expenses)</li>
 *   <li><b>Category:</b> Classification of the transaction (e.g., Food, Transportation)</li>
 *   <li><b>Timestamp:</b> Unix timestamp (milliseconds since epoch) when the transaction occurred</li>
 * </ul>
 *
 * <p><b>Usage in the Application:</b></p>
 * <ul>
 *   <li>Created via Quick Entry screen for expenses</li>
 *   <li>Displayed in History screen with filtering options</li>
 *   <li>Aggregated in Dashboard for pie charts and spending summaries</li>
 *   <li>Can be edited or deleted from History screen</li>
 * </ul>
 *
 * <p><b>Note on Transaction Types:</b></p>
 * Currently, the application primarily tracks expenses. Positive amounts
 * represent money spent. Future versions may support negative amounts or
 * a separate transaction type field for income entries.
 *
 * @version 1.0
 * @since 1.0
 * @see Category
 * @see com.example.masroofy.Model.History
 * @see com.example.masroofy.Model.QuickEntry
 * @see com.example.masroofy.Controller.QuickEntryController
 */
public class Transaction {

    /** The monetary amount of the transaction (positive for expenses). */
    private double transactionAmount;

    /** The spending category classification for this transaction. */
    private Category transactionCategory;

    /**
     * The timestamp of the transaction in milliseconds since Unix epoch.
     * <p>
     * Example: {@code System.currentTimeMillis()} returns the current time
     * in this format.
     * </p>
     */
    private long transactionTimestamp;

    /**
     * Constructs a Transaction with the specified amount, category, and timestamp.
     * <p>
     * This is the primary constructor for creating a new transaction record.
     * </p>
     *
     * @param transactionAmount the monetary amount of the transaction
     * @param transactionCategory the category classification of the transaction
     * @param transactionTimestamp the timestamp in milliseconds since Unix epoch
     */
    public Transaction(double transactionAmount, Category transactionCategory, long transactionTimestamp) {
        this.transactionAmount = transactionAmount;
        this.transactionCategory = transactionCategory;
        this.transactionTimestamp = transactionTimestamp;
    }

    /**
     * Constructs an empty Transaction object.
     * <p>
     * This default constructor is provided for frameworks that require
     * no-argument constructors (e.g., certain serialization libraries, ORM).
     * Fields should be set using their respective setters after construction.
     * </p>
     */
    public Transaction() {
        // Default constructor for frameworks requiring no-arg constructor
    }

    /**
     * Returns the transaction amount.
     *
     * @return the monetary amount of the transaction
     */
    public double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * Sets the transaction amount.
     *
     * @param transactionAmount the new monetary amount
     */
    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * Returns the transaction category.
     *
     * @return the {@link Category} object associated with this transaction
     */
    public Category getTransactionCategory() {
        return transactionCategory;
    }

    /**
     * Sets the transaction category.
     *
     * @param transactionCategory the new category for this transaction
     */
    public void setTransactionCategory(Category transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    /**
     * Returns the transaction timestamp.
     *
     * @return the timestamp in milliseconds since Unix epoch
     * @see System#currentTimeMillis()
     */
    public long getTransactionTimestamp() {
        return transactionTimestamp;
    }

    /**
     * Sets the transaction timestamp.
     *
     * @param transactionTimestamp the new timestamp in milliseconds since Unix epoch
     */
    public void setTransactionTimestamp(long transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }
}