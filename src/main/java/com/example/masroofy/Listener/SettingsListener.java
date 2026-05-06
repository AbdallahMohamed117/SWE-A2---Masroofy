package com.example.masroofy.Listener;

public interface SettingsListener {
    void onChangePinClicked(String currentPin, String newPin);
    void onClearCycleClicked();
    boolean onBackClicked();
}
