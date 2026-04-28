package com.example.masroofy.Model;

import com.example.masroofy.Database.DatabaseConnection;

public class Pin extends AbstractModel {
    protected Pin() {
        super();
    }

    public void setPin(int pin) {}

    public boolean checkPin(int pin) {
        return false;
    }
}