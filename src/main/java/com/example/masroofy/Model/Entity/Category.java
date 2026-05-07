package com.example.masroofy.Model.Entity;

/**
 * Represents an expense category in the Masroofy application.
 * <p>
 * The {@code Category} class encapsulates a spending category name used to classify
 * financial transactions. Categories allow users to organize their expenses
 * (e.g., Food, Transportation, Entertainment, Utilities) and view spending
 * breakdowns in pie charts and reports.
 * </p>
 *
 * <p><b>Usage:</b></p>
 * <ul>
 *   <li>Each {@link Transaction} is associated with one Category</li>
 *   <li>Categories appear in dropdown selectors on the Quick Entry screen</li>
 *   <li>Users can create custom categories during expense entry</li>
 *   <li>Pie charts on the Dashboard display spending by category</li>
 * </ul>
 *
 * <p><b>Example Category Names:</b></p>
 * <ul>
 *   <li>Food & Dining</li>
 *   <li>Transportation</li>
 *   <li>Shopping</li>
 *   <li>Entertainment</li>
 *   <li>Bills & Utilities</li>
 *   <li>Healthcare</li>
 *   <li>Education</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see Transaction
 * @see com.example.masroofy.Model.QuickEntry
 * @see com.example.masroofy.View.QuickEntryView
 */
public class Category {

    /** The name of the expense category. */
    private String categoryName;

    /**
     * Constructs an empty Category object.
     * <p>
     * This default constructor is provided for frameworks that require
     * no-argument constructors (e.g., certain serialization libraries).
     * The category name should be set using {@link #setCategoryName(String)}
     * after construction.
     * </p>
     */
    public Category() {
        // Default constructor for frameworks requiring no-arg constructor
    }

    /**
     * Constructs a Category with the specified name.
     * <p>
     * This is the primary constructor for creating a new category.
     * </p>
     *
     * @param categoryName the name of the expense category (e.g., "Food", "Transportation")
     */
    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Sets the name of the category.
     *
     * @param categoryName the new category name
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * Returns the name of the category.
     *
     * @return the category name
     */
    public String getCategoryName() {
        return this.categoryName;
    }
}