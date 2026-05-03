package com.example.masroofy;

import com.example.masroofy.Controller.*;
import com.example.masroofy.Model.*;
import com.example.masroofy.View.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HelloController {

    @FXML private ProgressBar progressBar;

    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(progressBar.progressProperty(), 1))
        );
        timeline.setOnFinished(e -> openSetupView());
        timeline.play();
    }

    private void openSetupView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/masroofy/View/PinView.fxml")
            );
            Parent root = loader.load();  // load once
            PinView view = loader.getController();
            Pin model = new Pin();
            PinController controller = new PinController(model, view);
            Scene scene = new Scene(root, 400, 650);  // use the same root
            Stage stage = (Stage) progressBar.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}