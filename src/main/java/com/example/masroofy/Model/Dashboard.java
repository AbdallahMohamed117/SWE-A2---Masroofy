package com.example.masroofy.Model;

import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;

import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
            if (resultDailyLimit.next()) {
                return resultDailyLimit.getDouble("daily_safe_limit");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Transaction> getPiechartData() {
        List<Transaction> transactions = new LinkedList<>();
        String getTransactionsQuery = "SELECT t.*, c.category_name " +
                "FROM Transactions t " +
                "JOIN Category c ON t.category_id = c.category_id " +
                "WHERE julianday('now') - julianday(t.transaction_timestamp / 1000, 'unixepoch') < 1";

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

    public int getDaysLeft() {
        String getEndDateQuery = "SELECT end_date FROM Budget";
        try (PreparedStatement prepareEndDate = connection.prepareStatement(getEndDateQuery)) {
            ResultSet result = prepareEndDate.executeQuery();
            if (result.next()) {
                String endDateStr = result.getString("end_date");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate = sdf.parse(endDateStr);
                LocalDate endDate = parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate today = LocalDate.now();
                long days = ChronoUnit.DAYS.between(today, endDate);
                return (int) Math.max(days, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
