package com.example.masroofy.View;

import java.util.Date;

public class SetupView implements AbstractView {
    private double allowanceInput;
    private Date startDate;
    private Date endDate;

    @Override
    public void printScreen() {}

    public void showSetupScreen() {}

    public void showErrorMessage(String msg) {}

    public void onStartCycleClicked(double amount, Date start, Date end) {}
}