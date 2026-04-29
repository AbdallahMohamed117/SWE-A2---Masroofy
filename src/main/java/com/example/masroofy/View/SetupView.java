package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SetupView implements AbstractView {

    @FXML private TextField etAllowanceAmount;
    @FXML private TextField etStartDate;
    @FXML private TextField etEndDate;
    @FXML private Label tvSetupError;
    @FXML private Button btnStartCycle;

    @Override
    public void printScreen() {}

    public void showSetupScreen() {}

    public void showErrorMessage(String msg) {
        tvSetupError.setText(msg);
        tvSetupError.setVisible(true);
    }

    @FXML
    public void onStartCycleClicked() {
        tvSetupError.setVisible(false);

        String amountText = etAllowanceAmount.getText();
        String startText  = etStartDate.getText();
        String endText    = etEndDate.getText();

        if (amountText == null || amountText.isEmpty()) {
            showErrorMessage("Please enter a valid amount.");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            showErrorMessage("Please enter a valid number.");
            return;
        }

        if (amount <= 0) {
            showErrorMessage("Allowance must be a positive number.");
            return;
        }

        if (startText == null || startText.isEmpty()) {
            showErrorMessage("Please enter a start date.");
            return;
        }

        if (endText == null || endText.isEmpty()) {
            showErrorMessage("Please enter an end date.");
            return;
        }

        LocalDate startDate;
        LocalDate endDate;
        try {
            startDate = LocalDate.parse(startText);
            endDate   = LocalDate.parse(endText);
        } catch (Exception e) {
            showErrorMessage("Invalid date format. Use YYYY-MM-DD.");
            return;
        }

        if (!endDate.isAfter(startDate)) {
            showErrorMessage("End date must be after start date.");
            return;
        }

        // Convert to Date
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end   = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        onStartCycleClicked(amount, start, end);
    }

    public void onStartCycleClicked(double amount, Date start, Date end) {
    }
}