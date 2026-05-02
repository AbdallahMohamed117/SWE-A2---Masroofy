package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.masroofy.Listener.SetupListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SetupView implements AbstractView {
    private SetupListener eventListener;
    @FXML private TextField etAllowanceAmount;
    @FXML private DatePicker etStartDate;
    @FXML private DatePicker etEndDate;
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

    public void setEventListener(SetupListener sl) {
        eventListener = sl;
    }
    public String getAmountText()    { return etAllowanceAmount.getText(); }
    public Date getStartDateText() {
        LocalDate lc = etStartDate.getValue();
        Date date = Date.from(lc.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }
    public Date getEndDateText()   {
        LocalDate lc = etEndDate.getValue();
        Date date = Date.from(lc.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    @FXML
    public void onStartCycleClicked() {


    }

    public void onSetupSubmitted(String amountText, String startText, String endText) {}
}
