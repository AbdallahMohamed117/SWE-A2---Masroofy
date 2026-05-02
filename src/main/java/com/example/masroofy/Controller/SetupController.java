package com.example.masroofy.Controller;

import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.Budget;
import com.example.masroofy.View.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;

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
    public boolean onSetupSumbitted(double allowance, Date startDate, Date endDate) {
        boolean pass = true;
        if(allowance < 0) {
            view.showErrorMessage("Invalid Allowance Amount");
            pass = false;
        }
        if(startDate.after(endDate)) {
            view.showErrorMessage("Start Date Can't Be Less Than End Date");
            pass = false;
        }
        if(pass) {
            Budget b = new Budget();
            b.setAllowance(allowance);
            b.setStartDate(startDate);
            b.setEndDate(endDate);
            b.setDailysafeLimit();

            model.setCycle(b);
        }
        return pass;
    }
}