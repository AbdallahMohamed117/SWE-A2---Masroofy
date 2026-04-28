package com.example.masroofy.Model;

import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class QuickEntry extends AbstractModel {
    protected QuickEntry() {
        super();
    }

    public void addTransaction(Transaction transaction) {

        String studentIdQuery         = "SELECT student_id FROM Student WHERE student_state = 'ACTIVE'";
        String categoryIdQuery        = "SELECT category_id FROM Category WHERE category_name = ?";
        String insertTransactionQuery = "INSERT INTO Transactions (transaction_amount, transaction_timestamp, student_id, category_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement studentStatement             = connection.prepareStatement(studentIdQuery);
             PreparedStatement getCategoryIdStatement       = connection.prepareStatement(categoryIdQuery);
             PreparedStatement insertTransactionStatement   = connection.prepareStatement(insertTransactionQuery))
        {
            ResultSet studentResult = studentStatement.executeQuery();
            if (studentResult.next()) {
                int studentId = studentResult.getInt("student_id");

                getCategoryIdStatement.setString(1, transaction.getTransactionCategory().getCategoryName());
                ResultSet categoryResult = getCategoryIdStatement.executeQuery();
                if (categoryResult.next()) {
                    int categoryId = categoryResult.getInt("category_id");

                    long timestamp = System.currentTimeMillis();

                    insertTransactionStatement.setDouble(1, transaction.getTransactionAmount());
                    insertTransactionStatement.setLong  (2, timestamp);
                    insertTransactionStatement.setInt   (3, studentId);
                    insertTransactionStatement.setInt   (4, categoryId);

                    insertTransactionStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCategory(Category category) {
        String insertCategoryQuery = "INSERT INTO Category (category_name) VALUES (?)";

        try (PreparedStatement insertCategoryStatement = connection.prepareStatement(insertCategoryQuery)) {
            insertCategoryStatement.setString(1, category.getCategoryName());
            insertCategoryStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCategory(Category category) {
        String deleteCategoryQuery = "DELETE FROM Category WHERE category_name = ?";

        try (PreparedStatement deleteCategoryStatement = connection.prepareStatement(deleteCategoryQuery)) {
            deleteCategoryStatement.setString(1, category.getCategoryName());
            deleteCategoryStatement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}