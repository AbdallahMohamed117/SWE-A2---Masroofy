package com.example.masroofy.App;

import com.example.masroofy.Controller.*;
import com.example.masroofy.View.*;
public class AppController implements AbstractController {
    private AbstractController currCtrl;
    private AppModel model;
    private AppView view;

    @Override
    public void PrintView(AbstractView view) {}

    public void switchCtrl() {}
}