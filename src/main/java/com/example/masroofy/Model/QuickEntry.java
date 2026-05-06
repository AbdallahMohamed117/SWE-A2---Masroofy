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
        String updateDailyLimitQuery = "UPDATE Budget SET daily_safe_limit = daily_safe_limit - ?";
        String studentIdQuery         = "SELECT student_id FROM Student WHERE student_state = 'ACTIVE'";
        String categoryIdQuery        = "SELECT category_id FROM Category WHERE category_name = ?";
        String insertCategoryQuery    = "INSERT INTO Category (category_name) VALUES (?)";
        String insertTransactionQuery = "INSERT INTO Transactions (transaction_amount, transaction_timestamp, student_id, category_id) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            double transactionAmount = transaction.getTransactionAmount();
            if (transactionAmount <= 0) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            int studentId = -1;
            try (PreparedStatement studentStatement = connection.prepareStatement(studentIdQuery)) {
                ResultSet studentResult = studentStatement.executeQuery();
                if (studentResult.next()) {
                    studentId = studentResult.getInt("student_id");
                }
            }
            if (studentId == -1) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

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

            if (transactionAmount > currentAllowance) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }

            int categoryId = -1;
            String categoryName = transaction.getTransactionCategory().getCategoryName();
            try (PreparedStatement getCategoryIdStatement = connection.prepareStatement(categoryIdQuery)) {
                getCategoryIdStatement.setString(1, categoryName);
                ResultSet categoryResult = getCategoryIdStatement.executeQuery();
                if (categoryResult.next()) {
                    categoryId = categoryResult.getInt("category_id");
                }
            }

            if (categoryId == -1) {
                try (PreparedStatement insertCategoryStatement = connection.prepareStatement(insertCategoryQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    insertCategoryStatement.setString(1, categoryName);
                    insertCategoryStatement.executeUpdate();
                    ResultSet generatedKeys = insertCategoryStatement.getGeneratedKeys();
                    generatedKeys.next();
                    categoryId = (int) generatedKeys.getLong(1);
                }
            }

            try (PreparedStatement updateAllowanceStmt = connection.prepareStatement(updateAllowanceQuery)) {
                updateAllowanceStmt.setDouble(1, transactionAmount);
                updateAllowanceStmt.executeUpdate();
            }

            try (PreparedStatement updateDailyLimitStmt = connection.prepareStatement(updateDailyLimitQuery)) {
                updateDailyLimitStmt.setDouble(1, transactionAmount);
                updateDailyLimitStmt.executeUpdate();
            }

            try (PreparedStatement insertTransactionStatement = connection.prepareStatement(insertTransactionQuery)) {
                insertTransactionStatement.setDouble(1, transactionAmount);
                insertTransactionStatement.setLong  (2, transaction.getTransactionTimestamp());
                insertTransactionStatement.setInt   (3, studentId);
                insertTransactionStatement.setInt   (4, categoryId);
                insertTransactionStatement.executeUpdate();
            }

            connection.commit();
            connection.setAutoCommit(true);
            return true;

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