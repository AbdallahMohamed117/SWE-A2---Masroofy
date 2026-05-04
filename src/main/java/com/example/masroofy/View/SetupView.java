package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.masroofy.Listener.SetupListener;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SetupView implements AbstractView {
    private SetupListener eventListener;
    private Runnable onNavigateToDashboard;
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

    public void setOnNavigateToDashboard(Runnable r) {
        this.onNavigateToDashboard = r;
    }

    public String getAmountText()    { return etAllowanceAmount.getText(); }
    public Date getStartDate() {
        LocalDate lc = etStartDate.getValue();
        Date date = Date.from(lc.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }
    public Date getEndDate()   {
        LocalDate lc = etEndDate.getValue();
        Date date = Date.from(lc.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    @FXML
    public void onStartCycleClicked() {

        double all = Double.parseDouble(getAmountText());
        Date start = getStartDate();
        Date end = getEndDate();

        if (eventListener.onSetupSumbitted(all, start, end)) {
            if (onNavigateToDashboard != null) onNavigateToDashboard.run();
        }
    }

}
