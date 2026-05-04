package com.example.masroofy;

import com.example.masroofy.App.AppController;
import com.example.masroofy.App.Screen;
import javafx.application.Application;
import javafx.stage.Stage;

public class BudgetApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        AppController appController = new AppController(primaryStage);
        appController.navigateTo(Screen.SPLASH);
        primaryStage.setTitle("Masroofy");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
