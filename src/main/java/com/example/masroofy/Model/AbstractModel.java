package com.example.masroofy.Model;

import com.example.masroofy.Database.*;

import java.sql.Connection;

/**
 * Abstract base class for all model classes in the Masroofy application.
 * <p>
 * The {@code AbstractModel} class provides common database connectivity to all
 * model classes that extend it. It ensures that every model has access to a
 * shared database connection instance, promoting code reuse and consistency
 * across the data access layer.
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Provides a protected {@link Connection} instance to all subclasses</li>
 *   <li>Uses the singleton-style connection from {@link DatabaseConnection}</li>
 *   <li>Ensures consistent database access across all models</li>
 * </ul>
 *
 * <p><b>Extending Classes:</b></p>
 * <ul>
 *   <li>{@link Dashboard} - Financial overview and statistics</li>
 *   <li>{@link History} - Transaction history and filtering</li>
 *   <li>{@link Pin} - PIN authentication and management</li>
 *   <li>{@link QuickEntry} - Transaction creation and category management</li>
 *   <li>{@link Setup} - Budget configuration and first-time setup</li>
 * </ul>
 *
 *
 * @version 1.0
 * @since 1.0
 * @see DatabaseConnection
 * @see Connection
 * @see Dashboard
 * @see History
 * @see Pin
 * @see QuickEntry
 * @see Setup
 */
public abstract class AbstractModel {

    /**
     * The database connection shared by all model subclasses.
     * <p>
     * This connection is obtained from {@link DatabaseConnection#getConnection()}
     * and is protected so that subclasses can use it for their database operations.
     * </p>
     */
    protected final Connection connection;

    /**
     * Constructs an AbstractModel and initializes the database connection.
     * <p>
     * The connection is obtained from the {@link DatabaseConnection} singleton,
     * ensuring that all models share the same database connection instance.
     * </p>
     * <p>
     * This constructor is protected because the class is abstract and should
     * only be called by subclasses.
     * </p>
     */
    protected AbstractModel() {
        this.connection = DatabaseConnection.getConnection();
    }
}