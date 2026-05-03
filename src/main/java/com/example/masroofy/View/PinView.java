package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.example.masroofy.Listener.PinListener;
public class PinView implements AbstractView {
    private PinListener pinListener;
    @FXML private Circle pinDot1;
    @FXML private Circle pinDot2;
    @FXML private Circle pinDot3;
    @FXML private Circle pinDot4;
    @FXML private Button btnSubmitPin;

    private final StringBuilder enteredPin = new StringBuilder();

    @Override
    public void printScreen() {
        showPinEntry();
    }
    public void setPinListener(PinListener pl) {
        pinListener = pl;
    }
    public String getEnteredPin() { return enteredPin.toString(); }

    // ── Display methods ────────────────────────────────────────────────────────
    public void showPinEntry() {
        enteredPin.setLength(0);
        updateDots();
    }

    public void showErrorMessage(String msg) {
        Color red = Color.web("#fb7185");
        pinDot1.setFill(red);
        pinDot2.setFill(red);
        pinDot3.setFill(red);
        pinDot4.setFill(red);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                javafx.application.Platform.runLater(() -> {
                    enteredPin.setLength(0);
                    updateDots();
                });
            } catch (InterruptedException ignored) {}
        }).start();
    }

    public void showLockout(int seconds) {
        btnSubmitPin.setDisable(true);
        btnSubmitPin.setText("Try again in " + seconds + "s");
        new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000L);
                javafx.application.Platform.runLater(() -> {
                    btnSubmitPin.setDisable(false);
                    btnSubmitPin.setText("UNLOCK NOW");
                    showPinEntry();
                });
            } catch (InterruptedException ignored) {}
        }).start();
    }

    @FXML
    public void initialize() {}

    @FXML
    private void onDigitClicked(javafx.event.ActionEvent e) {
        if (enteredPin.length() >= 4) return;
        enteredPin.append(((Button) e.getSource()).getText());
        updateDots();
    }

    @FXML
    private void onDeleteClicked() {
        if (enteredPin.length() == 0) return;
        enteredPin.deleteCharAt(enteredPin.length() - 1);
        updateDots();
    }

    @FXML
    private void onSubmitPin() {
        pinListener.onPinSubmitted(getEnteredPin());
    }
    private void updateDots() {
        Color filled = Color.web("#38bdf8");
        Color empty  = Color.web("#1e293b");
        pinDot1.setFill(enteredPin.length() >= 1 ? filled : empty);
        pinDot2.setFill(enteredPin.length() >= 2 ? filled : empty);
        pinDot3.setFill(enteredPin.length() >= 3 ? filled : empty);
        pinDot4.setFill(enteredPin.length() >= 4 ? filled : empty);
    }
}
