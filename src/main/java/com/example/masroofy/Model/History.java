package com.example.masroofy.Model;

import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class History extends AbstractModel {
    public History() {
        super();
    }

    public List<Transaction> getTransactions() {
        return getTransactions(null, null, null);
    }

    public List<Transaction> getTransactions(String category, Date from, Date to) {

        StringBuilder query = new StringBuilder(
                "SELECT t.transaction_amount, t.transaction_timestamp, c.category_name " +
                        "FROM Transactions t JOIN Category c ON t.category_id = c.category_id " +
                        "WHERE 1=1 "
        );

        if (category != null) query.append("AND c.category_name = ? ");
        if (from     != null) query.append("AND t.transaction_timestamp >= ? ");
        if (to       != null) query.append("AND t.transaction_timestamp <= ? ");

        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query.toString())) {

            int index = 1;
            if (category != null) stmt.setString(index++, category);
            if (from     != null) stmt.setLong  (index++, from.getTime());
            if (to       != null) stmt.setLong  (index++, to.getTime());

            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionAmount   (result.getDouble("transaction_amount"));
                transaction.setTransactionTimestamp(result.getLong  ("transaction_timestamp"));

                Category c = new Category();
                c.setCategoryName(result.getString("category_name"));
                transaction.setTransactionCategory(c);

                transactions.add(transaction);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public boolean editTransaction(Transaction transaction) {
        String getOldTransactionQuery = "SELECT transaction_amount FROM Transactions WHERE transaction_timestamp = ?";
        String getAllowanceQuery = "SELECT allowance FROM Budget";
        String updateAllowanceQuery = "UPDATE Budget SET allowance = allowance - ?";
        String addAllowanceQuery = "UPDATE Budget SET allowance = allowance + ?";
        String categoryIdQuery = "SELECT category_id FROM Category WHERE category_name = ?";
        String updateTransactionQuery = "UPDATE Transactions SET transaction_amount = ?, category_id = ? WHERE transaction_timestamp = ?";

        try {
            connection.setAutoCommit(false);

            double oldAmount = 0;
            try (PreparedStatement getOldStmt = connection.prepareStatement(getOldTransactionQuery)) {
                getOldStmt.setLong(1, transaction.getTransactionTimestamp());
                ResultSet oldRs = getOldStmt.executeQuery();
                if (oldRs.next()) {
                    oldAmount = oldRs.getDouble("transaction_amount");
                } else {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
            }

            double newAmount = transaction.getTransactionAmount();
            if (newAmount <= 0) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            double delta = newAmount - oldAmount;

            if (delta > 0) {
                double currentAllowance = 0;
                try (PreparedStatement getAllowanceStmt = connection.prepareStatement(getAllowanceQuery)) {
                    ResultSet allowanceRs = getAllowanceStmt.executeQuery();
                    if (allowanceRs.next()) {
                        currentAllowance = allowanceRs.getDouble("allowance");
                    } else {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        return false;
                    }
                }
                if (delta > currentAllowance) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    return false;
                }
                try (PreparedStatement updateAllowanceStmt = connection.prepareStatement(updateAllowanceQuery)) {
                    updateAllowanceStmt.setDouble(1, delta);
                    updateAllowanceStmt.executeUpdate();
                }
            } else if (delta < 0) {
                try (PreparedStatement addAllowanceStmt = connection.prepareStatement(addAllowanceQuery)) {
                    addAllowanceStmt.setDouble(1, Math.abs(delta));
                    addAllowanceStmt.executeUpdate();
                }
            }

            int categoryId = -1;
            try (PreparedStatement categoryIdStatement = connection.prepareStatement(categoryIdQuery)) {
                categoryIdStatement.setString(1, transaction.getTransactionCategory().getCategoryName());
                ResultSet categoryResult = categoryIdStatement.executeQuery();
                if (categoryResult.next()) {
                    categoryId = categoryResult.getInt("category_id");
                }
            }
            if (categoryId == -1) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            try (PreparedStatement updateTransactionStatement = connection.prepareStatement(updateTransactionQuery)) {
                updateTransactionStatement.setDouble(1, newAmount);
                updateTransactionStatement.setInt   (2, categoryId);
                updateTransactionStatement.setLong  (3, transaction.getTransactionTimestamp());
                int rows = updateTransactionStatement.executeUpdate();

                connection.commit();
                connection.setAutoCommit(true);
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }

    public boolean removeTransaction(Transaction transaction) {
        String deleteTransactionQuery = "DELETE FROM Transactions WHERE transaction_timestamp = ?";

        try (PreparedStatement deleteTransactionStatement = connection.prepareStatement(deleteTransactionQuery)) {
            deleteTransactionStatement.setLong(1, transaction.getTransactionTimestamp());

            return deleteTransactionStatement.executeUpdate() > 0;
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getCategories() {
        String getCategoriesQuery = "SELECT category_name FROM Category";
        List<String> categories = new ArrayList<>();
        try(PreparedStatement getCategoryStatement = connection.prepareStatement(getCategoriesQuery)) {
            ResultSet result = getCategoryStatement.executeQuery();
            while(result.next()) {
                categories.add(result.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
}