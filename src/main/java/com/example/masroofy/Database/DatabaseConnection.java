package com.example.masroofy.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the database connection for the Masroofy application.
 * <p>
 * The {@code DatabaseConnection} class provides a singleton-style connection
 * manager for SQLite database access. It ensures that a single connection
 * instance is reused throughout the application lifecycle, creating a new
 * connection only when necessary (first time or if the existing connection
 * is closed).
 * </p>
 *
 * <p><b>Database Details:</b></p>
 * <ul>
 *   <li>Database type: SQLite</li>
 *   <li>Database file: {@code masroofy.db} (created in the application working directory)</li>
 *   <li>Connection URL format: {@code jdbc:sqlite:masroofy.db}</li>
 * </ul>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Lazy initialization of database connection</li>
 *   <li>Connection reuse across multiple calls</li>
 *   <li>Automatic reconnection if connection is closed</li>
 *   <li>Runtime exception wrapping for SQL errors</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>
 * Connection conn = DatabaseConnection.getConnection();
 * PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Student");
 * ResultSet rs = stmt.executeQuery();
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 * @see Connection
 * @see DriverManager
 * @see SQLException
 */
public class DatabaseConnection {

    /** SQLite database connection URL pointing to the masroofy.db file. */
    private static final String URL = "jdbc:sqlite:masroofy.db";

    /** Singleton connection instance shared across the application. */
    private static Connection connection = null;

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This is a utility class with only static methods, so it should not be
     * instantiated.
     * </p>
     */
    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns a database connection to the SQLite database.
     * <p>
     * This method implements a lazy-initialization pattern:
     * </p>
     * <ul>
     *   <li>If no connection exists or the existing connection is closed,
     *       a new connection is established using {@link DriverManager}</li>
     *   <li>If a valid open connection already exists, it is returned directly</li>
     * </ul>
     *
     * <p><b>Error Handling:</b></p>
     * If a database access error occurs while establishing the connection,
     * a {@link RuntimeException} is thrown wrapping the original
     * {@link SQLException}. This simplifies error handling by avoiding
     * checked exceptions throughout the application.
     *
     * @return a valid {@link Connection} to the SQLite database
     * @throws RuntimeException if the database connection cannot be established
     *
     * @see DriverManager#getConnection(String)
     * @see Connection#isClosed()
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed: " + e.getMessage(), e);
        }
    }
}