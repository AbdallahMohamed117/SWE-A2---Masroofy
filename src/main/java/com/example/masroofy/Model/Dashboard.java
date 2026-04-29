package com.example.masroofy.Model;

import com.example.masroofy.Database.DatabaseConnection;
import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
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

    public List<Transaction> getPiechartData() {
        List<Transaction> transactions = new LinkedList<>();
        String getTransactionsQuery = "SELECT t.*, c.category_name " +
                "FROM Transactions t " +
                "JOIN Category c ON t.category_id = c.category_id " +
                "WHERE julianday('now') - julianday(DATE(t.transaction_timestamp)) = 0";

        try(PreparedStatement prepareTransactions = connection.prepareStatement(getTransactionsQuery)) {
            ResultSet resultTransactions = prepareTransactions.executeQuery();
            while(resultTransactions.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionAmount(resultTransactions.getDouble("transaction_amount"));
                transaction.setTransactionTimestamp(resultTransactions.getLong("transaction_timestamp"));

                Category category = new Category();
                category.setCategoryName(resultTransactions.getString("category_name"));
                transaction.setTransactionCategory(category);

                transactions.add(transaction);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
}