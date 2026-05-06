package com.example.masroofy.Model;

import com.example.masroofy.Model.Entity.Budget;

import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class Setup extends AbstractModel {

    public Setup() {
        super();
    }

    public void setCycle(Budget budget){

        String createBudgetQuery = "INSERT INTO Budget (allowance, start_date, end_date, daily_safe_limit, original_daily_limit, last_recalc_date) VALUES (?, ?, ?, ?, ?, ?)";
        String updateStudentQuery = "UPDATE Student SET student_state = 'ACTIVE'";

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try (PreparedStatement createBudgetStatement = connection.prepareStatement(createBudgetQuery);
             PreparedStatement updateStudentStateStatement = connection.prepareStatement(updateStudentQuery)) {

            createBudgetStatement.setDouble(1, budget.getAllowance());
            createBudgetStatement.setString(2, sdf.format(budget.getStartDate()));
            createBudgetStatement.setString(3, sdf.format(budget.getEndDate()));
            createBudgetStatement.setDouble(4, budget.getDailySafeLimit());
            createBudgetStatement.setDouble(5, budget.getDailySafeLimit());
            createBudgetStatement.setString(6, sdf.format(new java.util.Date()));

            createBudgetStatement.executeUpdate();
            updateStudentStateStatement.executeUpdate();

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
    public void clearCycle() {
        String deleteBudgetSql = "DELETE FROM Budget";
        String updateStudentSql = "UPDATE Student SET student_state = 'INACTIVE'";

        try (PreparedStatement deleteStmt = connection.prepareStatement(deleteBudgetSql);
             PreparedStatement updateStmt = connection.prepareStatement(updateStudentSql)) {

            updateStmt.executeUpdate();
            deleteStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object getCycle() {
        return null;
    }


}