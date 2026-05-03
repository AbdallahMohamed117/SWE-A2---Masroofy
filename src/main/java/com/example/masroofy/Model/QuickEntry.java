package com.example.masroofy.Model;

import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class QuickEntry extends AbstractModel {
    public QuickEntry() {
        super();
    }

    public void addTransaction(Transaction transaction) {

        String studentIdQuery         = "SELECT student_id FROM Student WHERE student_state = 'ACTIVE'";
        String categoryIdQuery        = "SELECT category_id FROM Category WHERE category_name = ?";
        String insertCategoryQuery    = "INSERT INTO Category (category_name) VALUES (?)";
        String insertTransactionQuery = "INSERT INTO Transactions (transaction_amount, transaction_timestamp, student_id, category_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement studentStatement           = connection.prepareStatement(studentIdQuery);
             PreparedStatement getCategoryIdStatement     = connection.prepareStatement(categoryIdQuery);
             PreparedStatement insertCategoryStatement    = connection.prepareStatement(insertCategoryQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement insertTransactionStatement = connection.prepareStatement(insertTransactionQuery))
        {
            ResultSet studentResult = studentStatement.executeQuery();
            if (studentResult.next()) {
                int studentId = studentResult.getInt("student_id");

                String categoryName = transaction.getTransactionCategory().getCategoryName();
                getCategoryIdStatement.setString(1, categoryName);
                ResultSet categoryResult = getCategoryIdStatement.executeQuery();

                int categoryId;
                if (categoryResult.next()) {
                    categoryId = categoryResult.getInt("category_id");
                } else {
                    insertCategoryStatement.setString(1, categoryName);
                    insertCategoryStatement.executeUpdate();
                    ResultSet generatedKeys = insertCategoryStatement.getGeneratedKeys();
                    generatedKeys.next();
                    categoryId = (int) generatedKeys.getLong(1);
                }

                insertTransactionStatement.setDouble(1, transaction.getTransactionAmount());
                insertTransactionStatement.setLong  (2, System.currentTimeMillis());
                insertTransactionStatement.setInt   (3, studentId);
                insertTransactionStatement.setInt   (4, categoryId);
                insertTransactionStatement.executeUpdate();
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