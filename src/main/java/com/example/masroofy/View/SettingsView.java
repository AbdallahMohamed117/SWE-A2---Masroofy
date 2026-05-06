package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsView implements AbstractView, Initializable {

    @FXML private TextField etCurrentPin;
    @FXML private TextField etNewPin;
    @FXML private Label tvPinError;
    @FXML private Label tvClearError;
    @FXML private Button btnChangePin;
    @FXML private Button btnClearCycle;

    private Runnable onNavigateBack;
    private Runnable onNavigateToSetup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tvPinError.setVisible(false);
        tvClearError.setVisible(false);
    }

    @Override
    public void printScreen() {}

    public void setOnNavigateBack(Runnable r) {
        this.onNavigateBack = r;
    }

    public void setOnNavigateToSetup(Runnable r) {
        this.onNavigateToSetup = r;
    }

    public String getCurrentPin() {
        return etCurrentPin.getText();
    }

    public String getNewPin() {
        return etNewPin.getText();
    }

    public void showPinError(String msg) {
        tvPinError.setText(msg);
        tvPinError.setVisible(true);
    }

    public void hidePinError() {
        tvPinError.setVisible(false);
    }

    public void showPinSuccess() {
        etCurrentPin.clear();
        etNewPin.clear();
        tvPinError.setVisible(false);
    }

    public void showClearError(String msg) {
        tvClearError.setText(msg);
        tvClearError.setVisible(true);
    }

    public void navigateToSetup() {
        if (onNavigateToSetup != null) onNavigateToSetup.run();
    }

    public void navigateBack() {
        if (onNavigateBack != null) onNavigateBack.run();
    }

    @FXML
    public void onChangePinClicked() {
        // Controller handles logic
    }

    @FXML
    public void onClearCycleClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Cycle");
        alert.setHeaderText("Are you sure?");
        alert.setContentText(
                "This will permanently delete all logs for this cycle."
        );
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Controller handles logic
            }
        });
    }

    @FXML
    public void onBackClicked() {
        navigateBack();
    }
}