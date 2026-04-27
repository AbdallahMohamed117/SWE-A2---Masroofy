package com.example.masroofy.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:masroofy.db";
    private static Connection connection = null;


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
