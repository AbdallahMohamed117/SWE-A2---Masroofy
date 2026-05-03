package com.example.masroofy.Controller;

import com.example.masroofy.Model.*;
import com.example.masroofy.View.*;
import com.example.masroofy.Listener.PinListener;
public class PinController implements AbstractController, PinListener {
    private Pin model;
    private PinView view;
    private int tries;
    public PinController(Pin m, PinView v) {
        model = m;
        view = v;
        view.setPinListener(this);
        tries = 0;
    }
    @Override
    public void PrintView() {}

    @Override
    public boolean onPinSubmitted(String pin) {
        boolean validate = true;
        if(model.pinExist()) {
            model.setPin(pin);
        }
        else if(!model.checkPin(pin)) {
            validate = false;
            ++tries;
            if(tries > 5) {
                view.showErrorMessage("Maximum Tries Reached, Wait 5 Minutes and Try Again");
                view.showLockout(300);
            }
            else {
                view.showErrorMessage("Please Enter The Correct Pin");
            }
        }
        else if(model.checkPin(pin)) {
            model.setPin(pin);
        }
        return validate;
    }
}