package com.example.masroofy;

import com.example.masroofy.Controller.*;
import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;
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
                    getClass().getResource("/com/example/masroofy/View/HistoryView.fxml")
            );
            Parent root = loader.load();
            HistoryView view = loader.getController();
            History model = new History();
            HistoryController controller = new HistoryController((History) model, (HistoryView) view);
            controller.PrintView();
            Scene scene = new Scene(root, 400, 650);
            Stage stage = (Stage) progressBar.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}