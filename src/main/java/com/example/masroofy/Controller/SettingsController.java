package com.example.masroofy.Controller;

import com.example.masroofy.Listener.SettingsListener;
import com.example.masroofy.Model.Pin;
import com.example.masroofy.Model.Setup;
import com.example.masroofy.View.SettingsView;

public class SettingsController implements AbstractController, SettingsListener {
    private Pin pinModel;
    private Setup setupModel;
    private SettingsView view;

    public SettingsController(Pin pinModel, Setup setupModel, SettingsView view) {
        this.pinModel = pinModel;
        this.setupModel = setupModel;
        this.view = view;
        view.setSettingsListener(this);
    }

    @Override
    public void PrintView() {}

    @Override
    public void onChangePinClicked(String currentPin, String newPin) {
        view.hidePinError();

        if (currentPin == null || currentPin.trim().isEmpty()) {
            view.showPinError("Current PIN is required.");
            return;
        }

        if (newPin == null || newPin.trim().isEmpty()) {
            view.showPinError("New PIN is required.");
            return;
        }

        if (!pinModel.checkPin(currentPin)) {
            view.showPinError("Current PIN is incorrect.");
            return;
        }

        if (newPin.length() < 4) {
            view.showPinError("New PIN must be at least 4 digits.");
            return;
        }

        boolean success = pinModel.updatePin(currentPin, newPin);
        if (success) {
            view.showPinSuccess();
            view.showSuccessMessage("PIN changed successfully!");
        } else {
            view.showPinError("Failed to update PIN. Please try again.");
        }
    }

    @Override
    public void onClearCycleClicked() {
        setupModel.clearCycle();
        view.navigateToSetup();
    }

    @Override
    public boolean onBackClicked() {
        return true;
    }
}
