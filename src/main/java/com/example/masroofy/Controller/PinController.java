package com.example.masroofy.Controller;

import com.example.masroofy.Model.*;
import com.example.masroofy.View.*;
import com.example.masroofy.Listener.PinListener;
public class PinController implements AbstractController, PinListener {
    private Pin model;
    private PinView view;

    public PinController(Pin m, PinView v) {
        model = m;
        view = v;
    }
    @Override
    public void PrintView() {}

    @Override
    public boolean onPinSubmitted(String pin) {
        return true;
    }

    public boolean validatePin(String pin) {
        return false;
    }
}