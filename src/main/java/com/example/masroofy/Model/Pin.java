package com.example.masroofy.Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pin extends AbstractModel {
    protected Pin() {
        super();
    }

    public void setPin(int pin) {
        String setPinQuery = "INSERT INTO Student (student_pincode, student_state, budget_id) VALUES (?, 'INACTIVE', ?)";

        try (PreparedStatement setPinStatement = connection.prepareStatement(setPinQuery)){
            setPinStatement.setInt(1, pin);
            setPinStatement.setInt(2,1 );

            setPinStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean checkPin(int enteredPin) {
        String checkPinQuery = "SELECT student_pincode FROM Student";

        try (PreparedStatement checkPinStatement = connection.prepareStatement(checkPinQuery)) {

            ResultSet result = checkPinStatement.executeQuery();

            if (result.next()){
                int storedPin = result.getInt("student_pincode");
                return storedPin == enteredPin;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}