package com.example.masroofy.util;

/**
 * Utility class for validating quick entry input fields.
 * <p>
 * The {@code QuickEntryValidator} provides input validation for the Quick Entry
 * screen, ensuring that expense amounts and category selections meet the
 * required criteria before being processed by the controller.
 * </p>
 *
 * <p><b>Validation Rules:</b></p>
 * <ul>
 *   <li>Amount text cannot be {@code null} or empty</li>
 *   <li>Amount must be a valid numeric value (parseable to double)</li>
 *   <li>Amount must be greater than zero (positive value)</li>
 *   <li>Category cannot be {@code null} or empty</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * QuickEntryValidator validator = new QuickEntryValidator();
 *
 * // Valid input
 * boolean isValid = validator.validate("25.50", "Food"); // returns true
 *
 * // Invalid input - negative amount
 * isValid = validator.validate("-10", "Food"); // returns false
 *
 * // Invalid input - empty category
 * isValid = validator.validate("25.50", ""); // returns false
 *
 * // Invalid input - non-numeric amount
 * isValid = validator.validate("abc", "Food"); // returns false
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 * @see com.example.masroofy.Controller.QuickEntryController
 * @see com.example.masroofy.Controller.QuickEntryEditController
 * @see com.example.masroofy.View.QuickEntryView
 */
public class QuickEntryValidator {

    /**
     * Validates the amount text and category for a quick entry submission.
     * <p>
     * This method performs comprehensive validation on both input fields
     * before they are used to create or update a transaction.
     * </p>
     *
     * <p><b>Validation Steps:</b></p>
     * <ol>
     *   <li>Check that amount text is not {@code null} or empty after trimming</li>
     *   <li>Attempt to parse amount text to a double value</li>
     *   <li>Verify that parsed amount is greater than zero</li>
     *   <li>Check that category is not {@code null} or empty after trimming</li>
     * </ol>
     *
     * <p><b>Return Value:</b></p>
     * <ul>
     *   <li>{@code true} - All validation checks pass</li>
     *   <li>{@code false} - Any validation check fails</li>
     * </ul>
     *
     * <p><b>Examples:</b></p>
     * <table border="1">
     *   <caption>Validation Examples</caption>
     *   <tr><th>amountText</th><th>category</th><th>Result</th><th>Reason</th></tr>
     *   <tr><td>"10.50"</td><td>"Food"</td><td>true</td><td>Valid input</td></tr>
     *   <tr><td>"0"</td><td>"Food"</td><td>false</td><td>Amount must be > 0</td></tr>
     *   <tr><td>"-5"</td><td>"Food"</td><td>false</td><td>Amount must be > 0</td></tr>
     *   <tr><td>"abc"</td><td>"Food"</td><td>false</td><td>Non-numeric amount</td></tr>
     *   <tr><td>""</td><td>"Food"</td><td>false</td><td>Empty amount</td></tr>
     *   <tr><td>"10.50"</td><td>""</td><td>false</td><td>Empty category</td></tr>
     *   <tr><td>"10.50"</td><td>null</td><td>false</td><td>Null category</td></tr>
     * </table>
     *
     * @param amountText the expense amount as text (must be a positive number)
     * @param category the expense category name (cannot be empty)
     * @return {@code true} if both amount and category are valid,
     *         {@code false} otherwise
     */
    public boolean validate(String amountText, String category) {
        // Validate amount text is not empty
        if (amountText == null || amountText.trim().isEmpty()) {
            return false;
        }

        // Validate amount is a positive number
        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        // Validate category is not empty
        if (category == null || category.trim().isEmpty()) {
            return false;
        }

        return true;
    }
}