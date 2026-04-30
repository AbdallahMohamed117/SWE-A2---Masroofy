package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SetupView implements AbstractView {

    @FXML private TextField etAllowanceAmount;
    @FXML private TextField etStartDate;
    @FXML private TextField etEndDate;
    @FXML private Label tvSetupError;
    @FXML private Button btnStartCycle;

    @Override
    public void printScreen() {
        tvSetupError.setVisible(false);
    }

    public void showSetupScreen() {}

    public void showErrorMessage(String msg) {
        tvSetupError.setText(msg);
        tvSetupError.setVisible(true);
    }

    public String getAmountText()    { return etAllowanceAmount.getText(); }
    public String getStartDateText() { return etStartDate.getText(); }
    public String getEndDateText()   { return etEndDate.getText(); }

    @FXML
    public void onStartCycleClicked() {
        onSetupSubmitted(getAmountText(), getStartDateText(), getEndDateText());
    }

    public void onSetupSubmitted(String amountText, String startText, String endText) {}
}