package com.example.masroofy.View;

public class PinView implements AbstractView {
    private String enteredPin;

    @Override
    public void printScreen() {}

    public void showPinEntry() {}

    public void showErrorMessage(String msg) {}

    public void showLockout(int seconds) {}

    public void onPinSubmitted(String pin) {}
}