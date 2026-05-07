package com.example.masroofy.App;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Manages the primary application window and screen transitions.
 * <p>
 * The {@code AppView} class is responsible for controlling the main {@link Stage}
 * of the JavaFX application. It provides functionality to switch between different
 * UI screens while maintaining window properties such as resizability, sizing,
 * and centering.
 * </p>
 *
 * <p>Typical usage:</p>
 * <pre>
 * Stage primaryStage = new Stage();
 * AppView appView = new AppView(primaryStage);
 * appView.switchTo(someRootNode, Screen.LOGIN);
 * primaryStage.show();
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 * @see Screen
 * @see Stage
 */
public class AppView {

    /** The primary stage (window) of the JavaFX application. */
    private final Stage primaryStage;

    /**
     * Constructs an {@code AppView} with the specified primary stage.
     * <p>
     * The constructor disables window resizing by setting the stage's
     * resizable property to {@code false}.
     * </p>
     *
     * @param primaryStage the main window (stage) of the JavaFX application
     * @throws NullPointerException if {@code primaryStage} is {@code null}
     */
    public AppView(Stage primaryStage) {
        if (primaryStage == null) {
            throw new NullPointerException("primaryStage cannot be null");
        }
        this.primaryStage = primaryStage;
        primaryStage.setResizable(false);
    }

    /**
     * Switches the current view to the specified UI root with the given screen dimensions.
     * <p>
     * If the primary stage already has a scene, this method simply replaces the scene's root node
     * with the new {@code root}. If no scene exists, a new scene is created using the provided
     * {@code root} and the width/height from the {@code screen} parameter.
     * </p>
     * <p>
     * After updating the scene (or creating a new one), the window is resized to fit its content
     * using {@link Stage#sizeToScene()} and then centered on the screen using
     * {@link Stage#centerOnScreen()}.
     * </p>
     *
     * @param root   the root node of the new screen (typically a layout or group)
     * @param screen the screen enumeration constant defining the desired width and height
     * @throws NullPointerException if {@code root} or {@code screen} is {@code null}
     *
     * @see Screen#getWidth()
     * @see Screen#getHeight()
     * @see Stage#sizeToScene()
     * @see Stage#centerOnScreen()
     */
    public void switchTo(Parent root, Screen screen) {
        if (root == null) {
            throw new NullPointerException("root cannot be null");
        }
        if (screen == null) {
            throw new NullPointerException("screen cannot be null");
        }

        Scene currentScene = primaryStage.getScene();
        if (currentScene != null) {
            currentScene.setRoot(root);
        } else {
            primaryStage.setScene(new Scene(root, screen.getWidth(), screen.getHeight()));
        }
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
    }
}