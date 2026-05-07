package com.example.masroofy;

import javafx.application.Application;

/**
 * Launcher class for the Masroofy Budget Application.
 * <p>
 * The {@code Launcher} class serves as an alternative entry point for the
 * JavaFX application. It provides a workaround for issues that can occur
 * when launching JavaFX applications directly from a main method in the
 * Application subclass due to module path or classpath configurations.
 * </p>
 *
 * <p><b>Why a Separate Launcher?</b></p>
 * <ul>
 *   <li>Some IDEs and build tools have issues launching JavaFX applications
 *       directly from a class that extends {@link Application}</li>
 *   <li>Using a separate launcher class ensures proper JavaFX toolkit
 *       initialization</li>
 *   <li>Provides a clean separation between application logic and launch
 *       mechanism</li>
 * </ul>
 *
 * <p><b>Launch Process:</b></p>
 * <ol>
 *   <li>Launcher.main() is called with command line arguments</li>
 *   <li>It calls {@link Application#launch(Class, String...)} with
 *       {@link BudgetApplication} as the application class</li>
 *   <li>JavaFX runtime initializes and starts BudgetApplication</li>
 * </ol>
 *
 * <p><b>Usage:</b></p>
 * This class can be used as the main class in:
 * <ul>
 *   <li>JAR file manifests</li>
 *   <li>IDE run configurations</li>
 *   <li>Build tool configurations (Maven, Gradle)</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see Application
 * @see BudgetApplication
 * @see com.example.masroofy.App.AppController
 */
public class Launcher {

    /**
     * Private constructor to prevent instantiation.
     * <p>
     * This is a utility class with only static methods, so it should not be
     * instantiated.
     * </p>
     */
    private Launcher() {
        // Private constructor to prevent instantiation
    }

    /**
     * The main method that launches the Masroofy Budget Application.
     * <p>
     * This method delegates to the JavaFX {@link Application#launch(Class, String...)}
     * method to start the {@link BudgetApplication} with the specified command
     * line arguments. This indirection allows the application to be launched
     * correctly in environments where direct launching of the Application
     * subclass might cause issues.
     * </p>
     *
     * <p><b>Alternative Launch Methods:</b></p>
     * <ul>
     *   <li>Direct: {@code BudgetApplication.main(args)} - may cause toolkit issues</li>
     *   <li>Preferred: {@code Launcher.main(args)} - ensures proper initialization</li>
     * </ul>
     *
     * <p><b>Command Line Arguments:</b></p>
     * This application does not currently process any command line arguments,
     * but they are passed through to the JavaFX runtime for potential future use.
     *
     * @param args command line arguments (not used by the application)
     * @see Application#launch(Class, String...)
     * @see BudgetApplication
     */
    public static void main(String[] args) {
        Application.launch(BudgetApplication.class, args);
    }
}