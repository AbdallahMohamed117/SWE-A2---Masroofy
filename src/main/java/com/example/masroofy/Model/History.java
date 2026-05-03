package com.example.masroofy.Model;

import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class History extends AbstractModel {
    public History() {
        super();
    }

    public List<Transaction> getTransactions() {

        String getTransactionsQuery = "SELECT t.transaction_amount, t.transaction_timestamp, c.category_name " +
                "FROM Transactions t JOIN Category c ON t.category_id = c.category_id";

        List<Transaction> transactions = new ArrayList<>();

        try (PreparedStatement getTransactionsStatement = connection.prepareStatement(getTransactionsQuery))
        {
            ResultSet result = getTransactionsStatement.executeQuery();
            while (result.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionAmount   (result.getDouble("transaction_amount"));
                transaction.setTransactionTimestamp(result.getLong  ("transaction_timestamp"));

                Category category = new Category();
                category.setCategoryName(result.getString("category_name"));
                transaction.setTransactionCategory(category);

                transactions.add(transaction);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public boolean editTransaction(Transaction transaction) {
        String categoryIdQuery = "SELECT category_id FROM Category WHERE category_name = ?";
        String updateTransactionQuery = "UPDATE Transactions SET transaction_amount = ?, category_id = ? WHERE transaction_timestamp = ?";

        try (PreparedStatement categoryIdStatement = connection.prepareStatement(categoryIdQuery);
             PreparedStatement updateTransactionStatement = connection.prepareStatement(updateTransactionQuery))
        {
            categoryIdStatement.setString(1, transaction.getTransactionCategory().getCategoryName());
            ResultSet categoryResult = categoryIdStatement.executeQuery();
            if (categoryResult.next()) {
                int categoryId = categoryResult.getInt("category_id");

                updateTransactionStatement.setDouble(1, transaction.getTransactionAmount());
                updateTransactionStatement.setInt   (2, categoryId);
                updateTransactionStatement.setLong  (3, transaction.getTransactionTimestamp());

                return updateTransactionStatement.executeUpdate() > 0;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
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
}