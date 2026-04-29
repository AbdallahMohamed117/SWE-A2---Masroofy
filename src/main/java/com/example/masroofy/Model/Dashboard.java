package com.example.masroofy.Model;

import com.example.masroofy.Database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AbstractModel {
    public Dashboard() {
        super();
    }

    public double getDailyLimit() {
        String getDailyLimitQuery = "SELECT daily_safe_limit FROM Budget";

        try (PreparedStatement prepareDailyLimit = connection.prepareStatement(getDailyLimitQuery)) {
            ResultSet resultDailyLimit = prepareDailyLimit.executeQuery();
            double dailyLimit = resultDailyLimit.getDouble("daily_safe_limit");
            return dailyLimit;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Daily Limit Doesn't exist
        return 0;
    }

    public List<Double> getPiechartData() {
        List<Double> amounts = new ArrayList<>();
        String getTransactionsQuery = "SELECT transaction_amount FROM Transactions " +
                "WHERE julianday('now') - julianday(DATE(transaction_timestamp)) IS 0";

        try(PreparedStatement prepareTransactions = connection.prepareStatement(getTransactionsQuery)) {
            ResultSet resultTransactions = prepareTransactions.executeQuery();
            while(resultTransactions.next()) {
                double transaction = resultTransactions.getDouble("transaction_amount");
                amounts.add(transaction);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return amounts;
    }
}