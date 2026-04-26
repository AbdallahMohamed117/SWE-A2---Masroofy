package com.example.masroofy.App;

import com.example.masroofy.Model.AbstractModel;

public class AppModel implements AbstractModel {
    private AbstractModel currModel;

    @Override
    public void setupConnection() {}

    public void switchModel() {}

    public Object getConnection() {
        return null;
    }
}