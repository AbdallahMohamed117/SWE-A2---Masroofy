package com.example.masroofy.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class Pin extends AbstractModel {
    public Pin() {
        super();
    }

    public void setPin(String pin) {
        String setPinQuery = "INSERT INTO Student (student_pincode, student_state, budget_id) VALUES (?, 'ACTIVE', ?)";

        try (PreparedStatement setPinStatement = connection.prepareStatement(setPinQuery)){
            setPinStatement.setString(1, pin);
            setPinStatement.setInt(2,1 );

            setPinStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean checkPin(String enteredPin) {
        String checkPinQuery = "SELECT student_pincode FROM Student";

        try (PreparedStatement checkPinStatement = connection.prepareStatement(checkPinQuery)) {

            ResultSet result = checkPinStatement.executeQuery();

            if (result.next()){
                String storedPin = result.getString("student_pincode");
                return Objects.equals(storedPin, enteredPin);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean pinExist() {
        String checkPin = "SELECT student_pincode FROM Student";

        try(PreparedStatement checkPinStatement = connection.prepareStatement(checkPin)) {
            ResultSet result = checkPinStatement.executeQuery();
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePin(String currentPin, String newPin) {
        if (!checkPin(currentPin)) return false;

        String updatePinQuery = "UPDATE Student SET student_pincode = ? WHERE student_pincode = ?";

        try (PreparedStatement updateStmt = connection.prepareStatement(updatePinQuery)) {
            updateStmt.setString(1, newPin);
            updateStmt.setString(2, currentPin);
            updateStmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUserActive() {
        String checkState = "SELECT student_state FROM Student LIMIT 1";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkState)) {
            ResultSet result = checkStmt.executeQuery();
            if (result.next()) {
                return "ACTIVE".equals(result.getString("student_state"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}