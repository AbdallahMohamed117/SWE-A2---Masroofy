package com.example.masroofy.Model;

import com.example.masroofy.Database.DatabaseConnection;
import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;

import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Dashboard extends AbstractModel {
    public Dashboard() {
        super();
    }

    public double getAllowance() {
        String query = "SELECT allowance FROM Budget";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                return result.getDouble("allowance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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

        StringBuilder query = new StringBuilder(
                "SELECT t.transaction_amount, t.transaction_timestamp, c.category_name " +
                        "FROM Transactions t JOIN Category c ON t.category_id = c.category_id " +
                        "WHERE 1=1 "
        );


        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {


            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionAmount(result.getDouble("transaction_amount"));
                transaction.setTransactionTimestamp(result.getLong("transaction_timestamp"));

                Category c = new Category();
                c.setCategoryName(result.getString("category_name"));
                transaction.setTransactionCategory(c);

                transactions.add(transaction);
            }
        } catch (SQLException e) {
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

    public void recalculateDailyLimitIfNewDay() {
        String checkRecalcQuery = "SELECT last_recalc_date, daily_safe_limit FROM Budget";
        String updateDailyLimitQuery = "UPDATE Budget SET daily_safe_limit = ?, original_daily_limit = ?, last_recalc_date = ?";
        String getAllowanceQuery = "SELECT allowance FROM Budget";

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new java.util.Date());

            PreparedStatement checkStmt = connection.prepareStatement(checkRecalcQuery);
            ResultSet result = checkStmt.executeQuery();

            if (result.next()) {
                String lastRecalcDate = result.getString("last_recalc_date");

                if (lastRecalcDate == null || !lastRecalcDate.equals(today)) {
                    double currentAllowance = 0;
                    try (PreparedStatement getAllowanceStmt = connection.prepareStatement(getAllowanceQuery)) {
                        ResultSet allowanceRs = getAllowanceStmt.executeQuery();
                        if (allowanceRs.next()) {
                            currentAllowance = allowanceRs.getDouble("allowance");
                        }
                    }

                    int daysLeft = getDaysLeft();
                    if (daysLeft > 0) {
                        double newDailyLimit = currentAllowance / daysLeft;

                        try (PreparedStatement updateStmt = connection.prepareStatement(updateDailyLimitQuery)) {
                            updateStmt.setDouble(1, newDailyLimit);
                            updateStmt.setDouble(2, newDailyLimit);
                            updateStmt.setString(3, today);
                            updateStmt.executeUpdate();
                        }
                    }
                }
            }
            checkStmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDailyLimitExceeded() {
        String query = "SELECT daily_safe_limit FROM Budget";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();
            if (result.next()) {
                double dailyLimit = result.getDouble("daily_safe_limit");
                return dailyLimit <= 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}