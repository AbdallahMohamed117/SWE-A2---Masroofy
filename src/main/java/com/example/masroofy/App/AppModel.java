package com.example.masroofy.App;

import com.example.masroofy.Database.DatabaseConnection;
import com.example.masroofy.Model.AbstractModel;
import com.example.masroofy.Model.Dashboard;

public class AppModel {
    private AbstractModel currModel;

    public void switchModel() {
        this.currModel = new Dashboard();
    }

    public Object getConnection() {
        return null;
    }
}