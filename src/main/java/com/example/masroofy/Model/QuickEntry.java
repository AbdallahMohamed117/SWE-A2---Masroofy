package com.example.masroofy.Model;

import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class QuickEntry extends AbstractModel {
    public QuickEntry() {
        super();
    }

    public boolean addTransaction(Transaction transaction) {
        String getAllowanceQuery = "SELECT allowance FROM Budget";
        String updateAllowanceQuery = "UPDATE Budget SET allowance = allowance - ?";
        String studentIdQuery         = "SELECT student_id FROM Student WHERE student_state = 'ACTIVE'";
        String categoryIdQuery        = "SELECT category_id FROM Category WHERE category_name = ?";
        String insertCategoryQuery    = "INSERT INTO Category (category_name) VALUES (?)";
        String insertTransactionQuery = "INSERT INTO Transactions (transaction_amount, transaction_timestamp, student_id, category_id) VALUES (?, ?, ?, ?)";

        try (PreparedStatement getAllowanceStmt = connection.prepareStatement(getAllowanceQuery)) {
            ResultSet allowanceRs = getAllowanceStmt.executeQuery();
            double currentAllowance = 0;
            if (allowanceRs.next()) {
                currentAllowance = allowanceRs.getDouble("allowance");
            } else {
                return false;
            }

            double transactionAmount = transaction.getTransactionAmount();
            if (transactionAmount > currentAllowance) {
                return false;
            }

            try (PreparedStatement updateAllowanceStmt = connection.prepareStatement(updateAllowanceQuery)) {
                updateAllowanceStmt.setDouble(1, transactionAmount);
                updateAllowanceStmt.executeUpdate();
            }

            try (PreparedStatement studentStatement           = connection.prepareStatement(studentIdQuery);
                 PreparedStatement getCategoryIdStatement     = connection.prepareStatement(categoryIdQuery);
                 PreparedStatement insertCategoryStatement    = connection.prepareStatement(insertCategoryQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement insertTransactionStatement = connection.prepareStatement(insertTransactionQuery)) {

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

                    insertTransactionStatement.setDouble(1, transactionAmount);
                    insertTransactionStatement.setLong  (2, System.currentTimeMillis());
                    insertTransactionStatement.setInt   (3, studentId);
                    insertTransactionStatement.setInt   (4, categoryId);
                    insertTransactionStatement.executeUpdate();

                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean addCategory(Category category) {
        String checkCategoryQuery = "SELECT COUNT(*) FROM Category WHERE category_name = ?";
        String insertCategoryQuery = "INSERT INTO Category (category_name) VALUES (?)";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkCategoryQuery)) {
            checkStmt.setString(1, category.getCategoryName());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try (PreparedStatement insertCategoryStatement = connection.prepareStatement(insertCategoryQuery)) {
            insertCategoryStatement.setString(1, category.getCategoryName());
            insertCategoryStatement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
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

    public List<Category> getCategories() {
        String getCategoriesQuery = "SELECT * FROM Category";
        List<Category> categories = new ArrayList<>();

        try (PreparedStatement getCategoriesStatement = connection.prepareStatement(getCategoriesQuery);
             ResultSet resultSet = getCategoriesStatement.executeQuery()) {

            while (resultSet.next()) {
                Category category = new Category();
                category.setCategoryName(resultSet.getString("category_name"));
                categories.add(category);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
}