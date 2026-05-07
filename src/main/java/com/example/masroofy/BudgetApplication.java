package com.example.masroofy;

import com.example.masroofy.App.AppController;
import com.example.masroofy.App.Screen;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main entry point for the Masroofy Budget Application.
 * <p>
 * The {@code BudgetApplication} class is the primary JavaFX application class
 * that launches the Masroofy personal finance management application. It
 * initializes the main application window, creates the application controller,
 * and displays the splash screen to start the user experience.
 * </p>
 *
 * <p><b>Application Flow:</b></p>
 * <ol>
 *   <li>Application is launched via {@link #main(String[])}</li>
 *   <li>JavaFX runtime calls {@link #start(Stage)}</li>
 *   <li>AppController is created with the primary stage</li>
 *   <li>Navigation to the Splash screen is initiated</li>
 *   <li>Primary stage is configured and displayed</li>
 *   <li>Splash screen animation starts automatically</li>
 *   <li>Based on user state, navigation continues to PIN or Setup screens</li>
 * </ol>
 *
 * <p><b>Window Configuration:</b></p>
 * <ul>
 *   <li>Title: "Masroofy"</li>
 *   <li>Resizable: Disabled (fixed window size)</li>
 *   <li>Initial screen: Splash (500×750 pixels)</li>
 * </ul>
 *
 * <p><b>Class Hierarchy:</b></p>
 * {@code BudgetApplication} extends {@link Application} and overrides
 * the {@link #start(Stage)} method as required by JavaFX.
 *
 * @version 1.0
 * @since 1.0
 * @see Application
 * @see AppController
 * @see Screen
 * @see com.example.masroofy.App.SplashView
 */
public class BudgetApplication extends Application {

    /**
     * The main entry point for the JavaFX application.
     * <p>
     * This method is called automatically by the JavaFX runtime after the
     * application has been launched. It sets up the primary stage, initializes
     * the application controller, and displays the splash screen.
     * </p>
     *
     * <p><b>Stage Configuration:</b></p>
     * <ul>
     *   <li>Creates an {@link AppController} instance to manage navigation</li>
     *   <li>Navigates to the {@link Screen#SPLASH} screen</li>
     *   <li>Sets the window title to "Masroofy"</li>
     *   <li>Disables window resizing to maintain UI consistency</li>
     *   <li>Makes the stage visible to the user</li>
     * </ul>
     *
     * @param primaryStage the primary stage (window) provided by JavaFX
     * @see AppController#navigateTo(Screen)
     * @see Stage#setTitle(String)
     * @see Stage#setResizable(boolean)
     * @see Stage#show()
     */
    @Override
    public void start(Stage primaryStage) {
        AppController appController = new AppController(primaryStage);
        appController.navigateTo(Screen.SPLASH);
        primaryStage.setTitle("Masroofy");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * The main method that launches the JavaFX application.
     * <p>
     * This method is the standard Java entry point that calls
     * {@link Application#launch(String...)} to start the JavaFX runtime.
     * The {@code launch} method does not return until the application exits.
     * </p>
     *
     * <p><b>Command Line Arguments:</b></p>
     * This application does not currently process any command line arguments,
     * but they are passed through to the JavaFX runtime for potential future use.
     *
     * @param args command line arguments (not used)
     * @see Application#launch(String...)
     */
    public static void main(String[] args) {
        launch(args);
    }
}