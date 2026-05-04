package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import com.example.masroofy.Listener.PinListener;

public class PinView implements AbstractView {

    private static final Color FILLED = Color.web("#38bdf8");
    private static final Color EMPTY  = Color.web("#1e293b");
    private static final Color ERROR  = Color.web("#fb7185");

    private PinListener pinListener;
    private Runnable onNavigateToDashboard;

    @FXML private Circle pinDot1;
    @FXML private Circle pinDot2;
    @FXML private Circle pinDot3;
    @FXML private Circle pinDot4;
    @FXML private Button btnSubmitPin;

    private final Circle[] dots = new Circle[4];
    private final StringBuilder enteredPin = new StringBuilder();

    @FXML
    public void initialize() {
        dots[0] = pinDot1;
        dots[1] = pinDot2;
        dots[2] = pinDot3;
        dots[3] = pinDot4;
    }

    @Override
    public void printScreen() {
        showPinEntry();
    }

    public void setPinListener(PinListener pl) {
        pinListener = pl;
    }

    public void setOnNavigateToDashboard(Runnable r) {
        this.onNavigateToDashboard = r;
    }

    public String getEnteredPin() {
        return enteredPin.toString();
    }

    public void showPinEntry() {
        enteredPin.setLength(0);
        updateDots(EMPTY);
    }

    public void showErrorMessage(String msg) {
        updateDots(ERROR);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                javafx.application.Platform.runLater(() -> {
                    enteredPin.setLength(0);
                    updateDots(EMPTY);
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
    private void onDigitClicked(javafx.event.ActionEvent e) {
        if (enteredPin.length() >= 4) return;
        enteredPin.append(((Button) e.getSource()).getText());
        updateDot(enteredPin.length() - 1, FILLED);
    }

    @FXML
    private void onDeleteClicked() {
        if (enteredPin.length() == 0) return;
        enteredPin.deleteCharAt(enteredPin.length() - 1);
        updateDot(enteredPin.length(), EMPTY);
    }

    @FXML
    private void onSubmitPin() {
        if(pinListener.onPinSubmitted(getEnteredPin())) {
            updateDots(Color.web("#10B981"));
            if (onNavigateToDashboard != null) onNavigateToDashboard.run();
        }
    }

    private void updateDot(int index, Color color) {
        dots[index].setFill(color);
    }

    private void updateDots(Color color) {
        for (Circle dot : dots) {
            dot.setFill(color);
        }
    }
}
