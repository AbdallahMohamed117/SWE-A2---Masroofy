package com.example.masroofy.App;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppView {
    private final Stage primaryStage;

    public AppView(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void switchTo(Parent root, Screen screen) {
        primaryStage.setWidth(screen.getWidth());
        primaryStage.setHeight(screen.getHeight());
        Scene currentScene = primaryStage.getScene();
        if (currentScene != null) {
            currentScene.setRoot(root);
        } else {
            primaryStage.setScene(new Scene(root, screen.getWidth(), screen.getHeight()));
        }
    }
}
