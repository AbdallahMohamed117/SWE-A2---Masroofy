package com.example.masroofy.App;

import com.example.masroofy.Database.DatabaseConnection;
import com.example.masroofy.Model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Central data model for the Masroofy application.
 * <p>
 * The {@code AppModel} class serves as the main entry point for accessing all data models
 * in the application. It implements a lazy-loading pattern where model instances are
 * created only when first requested, improving startup performance and resource usage.
 * </p>
 *
 * <p>This class provides access to the following models:</p>
 * <ul>
 *   <li>{@link Dashboard} - for financial overview and statistics</li>
 *   <li>{@link QuickEntry} - for adding new transactions</li>
 *   <li>{@link History} - for viewing and managing transaction history</li>
 *   <li>{@link Pin} - for PIN authentication and security</li>
 *   <li>{@link Setup} - for application configuration and first-time setup</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see Dashboard
 * @see QuickEntry
 * @see History
 * @see Pin
 * @see Setup
 * @see DatabaseConnection
 */
public class AppModel {

    // Model instances (lazy-loaded)
    private Dashboard dashboard;
    private QuickEntry quickEntry;
    private History history;
    private Pin pin;
    private Setup setup;

    /**
     * Checks whether a PIN has already been set up in the application.
     * <p>
     * This method queries the {@code Student} table to determine if a PIN code exists.
     * The presence of at least one student record with a PIN code indicates that the
     * application has been configured and is ready for PIN-based authentication.
     * </p>
     *
     * @return {@code true} if a PIN exists in the database, {@code false} otherwise
     * @throws SQLException if a database access error occurs (caught and logged internally)
     */
    public boolean hasPin() {
        String query = "SELECT student_pincode FROM Student LIMIT 1";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns the Dashboard model instance.
     * <p>
     * Creates a new {@link Dashboard} instance if one does not already exist
     * (lazy initialization). The Dashboard provides financial summary data
     * including total income, expenses, and balance.
     * </p>
     *
     * @return the Dashboard model instance (never {@code null})
     * @see Dashboard
     */
    public Dashboard getDashboard() {
        if (dashboard == null) dashboard = new Dashboard();
        return dashboard;
    }

    /**
     * Returns the QuickEntry model instance.
     * <p>
     * Creates a new {@link QuickEntry} instance if one does not already exist
     * (lazy initialization). The QuickEntry model handles the creation of new
     * financial transactions including income and expense entries.
     * </p>
     *
     * @return the QuickEntry model instance (never {@code null})
     * @see QuickEntry
     */
    public QuickEntry getQuickEntry() {
        if (quickEntry == null) quickEntry = new QuickEntry();
        return quickEntry;
    }

    /**
     * Returns the History model instance.
     * <p>
     * Creates a new {@link History} instance if one does not already exist
     * (lazy initialization). The History model provides access to past transactions
     * with filtering, sorting, and editing capabilities.
     * </p>
     *
     * @return the History model instance (never {@code null})
     * @see History
     */
    public History getHistory() {
        if (history == null) history = new History();
        return history;
    }

    /**
     * Returns the Pin model instance.
     * <p>
     * Creates a new {@link Pin} instance if one does not already exist
     * (lazy initialization). The Pin model handles PIN verification, validation,
     * and user session management.
     * </p>
     *
     * @return the Pin model instance (never {@code null})
     * @see Pin
     */
    public Pin getPin() {
        if (pin == null) pin = new Pin();
        return pin;
    }

    /**
     * Returns the Setup model instance.
     * <p>
     * Creates a new {@link Setup} instance if one does not already exist
     * (lazy initialization). The Setup model handles first-time application
     * configuration including student registration and initial settings.
     * </p>
     *
     * @return the Setup model instance (never {@code null})
     * @see Setup
     */
    public Setup getSetup() {
        if (setup == null) setup = new Setup();
        return setup;
    }
}