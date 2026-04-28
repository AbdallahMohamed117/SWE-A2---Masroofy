package com.example.masroofy.Model;
import com.example.masroofy.Database.*;


import java.sql.Connection;

public abstract class AbstractModel {
    protected final Connection connection;

    protected AbstractModel() {
        this.connection = DatabaseConnection.getConnection();
    }
}
