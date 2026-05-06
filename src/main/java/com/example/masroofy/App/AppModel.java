package com.example.masroofy.App;

import com.example.masroofy.Database.DatabaseConnection;
import com.example.masroofy.Model.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AppModel {
    private Dashboard dashboard;
    private QuickEntry quickEntry;
    private History history;
    private Pin pin;
    private Setup setup;

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

    public boolean hasBudget() {
        String query = "SELECT budget_id FROM Budget LIMIT 1";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Dashboard getDashboard() {
        if (dashboard == null) dashboard = new Dashboard();
        return dashboard;
    }

    public QuickEntry getQuickEntry() {
        if (quickEntry == null) quickEntry = new QuickEntry();
        return quickEntry;
    }

    public History getHistory() {
        if (history == null) history = new History();
        return history;
    }

    public Pin getPin() {
        if (pin == null) pin = new Pin();
        return pin;
    }

    public Setup getSetup() {
        if (setup == null) setup = new Setup();
        return setup;
    }
}
