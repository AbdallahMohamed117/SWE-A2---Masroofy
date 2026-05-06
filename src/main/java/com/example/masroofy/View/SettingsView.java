package com.example.masroofy.View;

import com.example.masroofy.Listener.SettingsListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class SettingsView implements AbstractView {

    @FXML private TextField etCurrentPin;
    @FXML private TextField etNewPin;
    @FXML private Label tvPinError;
    @FXML private Label tvClearError;
    @FXML private Button btnChangePin;
    @FXML private Button btnClearCycle;

    private SettingsListener listener;
    private Runnable onNavigateBack;
    private Runnable onNavigateToSetup;

    @FXML
    public void initialize() {
        tvPinError.setVisible(false);
        tvClearError.setVisible(false);
    }

    @Override
    public void printScreen() {}

    public void setSettingsListener(SettingsListener listener) {
        this.listener = listener;
    }

    public void setOnNavigateBack(Runnable r) { this.onNavigateBack = r; }
    public void setOnNavigateToSetup(Runnable r) { this.onNavigateToSetup = r; }

    public String getCurrentPin() { return etCurrentPin.getText(); }
    public String getNewPin() { return etNewPin.getText(); }

    public void showPinError(String msg) {
        tvPinError.setText(msg);
        tvPinError.setStyle("-fx-text-fill: #fb7185;");
        tvPinError.setVisible(true);
    }

    public void hidePinError() { tvPinError.setVisible(false); }

    public void showPinSuccess() {
        etCurrentPin.clear();
        etNewPin.clear();
        tvPinError.setVisible(false);
    }

    public void showSuccessMessage(String msg) {
        tvPinError.setText(msg);
        tvPinError.setStyle("-fx-text-fill: #10b981;");
        tvPinError.setVisible(true);
    }

    public void showClearError(String msg) {
        tvClearError.setText(msg);
        tvClearError.setVisible(true);
    }

    public void navigateToSetup() {
        if (onNavigateToSetup != null) onNavigateToSetup.run();
    }

    @FXML
    public void onChangePinClicked() {
        if (listener != null) {
            listener.onChangePinClicked(getCurrentPin(), getNewPin());
        }
    }

    @FXML
    public void onClearCycleClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Cycle");
        alert.setHeaderText("Are you sure?");
        alert.setContentText("This will permanently delete all your data and start fresh.");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (listener != null) {
                    listener.onClearCycleClicked();
                }
            }
        });
    }

    @FXML
    public void onBackClicked() {
        if (listener != null && listener.onBackClicked()) {
            if (onNavigateBack != null) onNavigateBack.run();
        }
    }
}
