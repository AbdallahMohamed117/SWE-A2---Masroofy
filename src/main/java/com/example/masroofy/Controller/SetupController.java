package com.example.masroofy.Controller;

import com.example.masroofy.Model.*;
import com.example.masroofy.View.*;
import java.util.Date;
import com.example.masroofy.Listener.SetupListener;
public class SetupController implements AbstractController, SetupListener {
    private Setup model;
    private SetupView view;

    public SetupController(Setup m, SetupView v) {
        model = m;
        view = v;
        view.setEventListener(this);
    }

    @Override
    public void PrintView() {
        view.printScreen();
    }

    @Override
    public void onSetupSumbitted(double allowance, Date startDate, Date endDate) {

    }
    public void validateSetup(int allowanceAmount, Date startDate, Date endDate) {}
}