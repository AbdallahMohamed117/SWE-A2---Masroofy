package com.example.masroofy.App;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 * View class for the splash screen of the Masroofy application.
 * <p>
 * The {@code SplashView} creates and manages the application's launch splash screen,
 * featuring a modern dark-themed UI with animated logo, progress bar, and loading
 * animation. This screen is displayed while the application initializes and
 * determines whether to show the PIN screen or Setup screen.
 * </p>
 *
 * <p><b>UI Components:</b></p>
 * <ul>
 *   <li>Animated progress bar showing loading progress over 3 seconds</li>
 *   <li>Application logo (MASROOFY) with glow effects</li>
 *   <li>Glass-panel container with rounded corners and border effects</li>
 *   <li>Decorative background circles for visual depth</li>
 *   <li>Loading status label</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AppController
 * @see Timeline
 * @see ProgressBar
 */
public class SplashView {

    /** The progress bar that animates from 0% to 100% during splash display. */
    private ProgressBar progressBar;

    /**
     * Creates and configures the root UI element for the splash screen.
     * <p>
     * This method builds the complete splash screen layout including:
     * <ul>
     *   <li>A dark background (#0f172a)</li>
     *   <li>Two decorative translucent circles</li>
     *   <li>A glass-panel container with rounded corners</li>
     *   <li>Title label "MASROOFY" with teal color and drop shadow</li>
     *   <li>Subtitle "Wealth Builders Team"</li>
     *   <li>Animated logo area with SVG icon and "EGP" label</li>
     *   <li>Progress bar and loading text</li>
     * </ul>
     * </p>
     *
     * @return the root {@link Parent} node containing the complete splash screen UI
     */
    public Parent createRoot() {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(500, 750);
        root.setStyle("-fx-background-color: #0f172a;");

        // Decorative background circles
        Circle bgCircle1 = new Circle(350, 80, 120, Color.web("#0ea5e9", 0.1));
        Circle bgCircle2 = new Circle(50, 550, 150, Color.web("#0d9488", 0.08));
        root.getChildren().addAll(bgCircle1, bgCircle2);

        // Main container with glass panel effect
        VBox container = new VBox(15);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(10, 10, 10, 10));
        container.setLayoutX(30);
        container.setLayoutY(34);
        container.setPrefWidth(440);
        container.setPrefHeight(670);
        container.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.03);" +
                        "-fx-background-radius: 30;" +
                        "-fx-border-color: rgba(255, 255, 255, 0.08);" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 30;"
        );

        // Title section
        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("MASROOFY");
        titleLabel.setStyle("-fx-font-size: 42; -fx-font-weight: bold;");
        titleLabel.setTextFill(Color.web("#2dd4bf"));
        titleLabel.setEffect(new DropShadow(15, 0, 0, Color.web("#2dd4bf", 0.27)));

        Label subtitleLabel = new Label("Wealth Builders Team");
        subtitleLabel.setTextFill(Color.web("#94a3b8"));
        subtitleLabel.setFont(Font.font(16));

        titleBox.getChildren().addAll(titleLabel, subtitleLabel);

        // Logo area with animated SVG icon
        StackPane logoArea = new StackPane();
        logoArea.setPrefHeight(336);
        logoArea.setPrefWidth(365);

        Circle logoBg1 = new Circle(0, 0, 80, Color.web("#0ea5e9", 0.05));
        Circle logoBg2 = new Circle(0, 0, 60, Color.web("#0d9488", 0.1));
        logoBg2.setEffect(new DropShadow(30, 0, 0, Color.web("#0d9488", 0.2)));

        SVGPath logoIcon = new SVGPath();
        logoIcon.setContent("M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-2h2v2zm0-4h-2V7h2v6z M13.5 10.5l-1.5-1.5-3 3L11 14l1.5-1.5L14 14l1.5-1.5-2-2z");
        logoIcon.setFill(Color.web("#2dd4bf"));
        logoIcon.setFillRule(FillRule.EVEN_ODD);
        logoIcon.setScaleX(3.5);
        logoIcon.setScaleY(3.5);
        logoIcon.setStroke(Color.web("#301e1e"));
        logoIcon.setEffect(new DropShadow(20, 0, 0, Color.web("#2dd4bf", 0.4)));

        Label egpLabel = new Label("EGP");
        egpLabel.setTextFill(Color.WHITE);
        egpLabel.setFont(Font.font("System Bold", 14));
        egpLabel.setTranslateY(45);

        logoArea.getChildren().addAll(logoBg1, logoBg2, logoIcon, egpLabel);

        // Progress section
        progressBar = new ProgressBar();
        progressBar.setPrefHeight(8);
        progressBar.setPrefWidth(260);
        progressBar.setProgress(0);
        progressBar.setStyle(
                "-fx-accent: #0d9488;" +
                        "-fx-control-inner-background: rgba(255,255,255,0.05);" +
                        "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10;"
        );

        Label loadingLabel = new Label("Loading your financial world...");
        loadingLabel.setTextFill(Color.web("#64748b"));
        loadingLabel.setFont(Font.font("System Italic", 13));

        VBox progressBox = new VBox(20);
        progressBox.setAlignment(Pos.CENTER);
        progressBox.setPrefWidth(280);
        progressBox.getChildren().addAll(progressBar, loadingLabel);
        VBox.setMargin(progressBox, new Insets(20, 0, 0, 0));

        container.getChildren().addAll(titleBox, logoArea, progressBox);
        root.getChildren().add(container);

        return root;
    }

    /**
     * Starts the splash screen loading animation.
     * <p>
     * This method animates the progress bar from 0% to 100% over a duration of
     * 3 seconds using a {@link Timeline}. When the animation completes, the
     * provided {@code onFinish} callback is executed, typically to navigate to
     * the next screen (PIN or Setup) based on the application's state.
     * </p>
     *
     * <p>The animation uses {@link KeyFrame} and {@link KeyValue} to smoothly
     * transition the progress bar's value property.</p>
     *
     * @param onFinish the {@link Runnable} to execute when the animation completes;
     *                 may be {@code null} if no action is needed
     */
    public void startAnimation(Runnable onFinish) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(progressBar.progressProperty(), 1))
        );
        timeline.setOnFinished(e -> {
            if (onFinish != null)
                onFinish.run();
        });
        timeline.play();
    }
}