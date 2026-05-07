package com.example.masroofy.App;

/**
 * Enumeration of all screens available in the Masroofy application.
 * <p>
 * The {@code Screen} enum defines the complete set of user interface screens
 * in the application. Each enum constant contains metadata about the screen
 * including its display name, FXML file path, and dimensions (width and height).
 * </p>
 *
 * <p>This enum is used primarily by {@link AppController} and {@link AppView}
 * for screen navigation and view management.</p>
 *
 * <p><b>Available Screens:</b></p>
 * <ul>
 *   <li>{@link #SPLASH} - Launch splash screen with animation</li>
 *   <li>{@link #PIN} - PIN entry/authentication screen</li>
 *   <li>{@link #SETUP} - First-time application setup screen</li>
 *   <li>{@link #DASHBOARD} - Main dashboard with financial overview</li>
 *   <li>{@link #QUICK_ENTRY} - Fast transaction entry screen</li>
 *   <li>{@link #HISTORY} - Transaction history viewer</li>
 *   <li>{@link #SETTINGS} - Application settings and preferences</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AppController
 * @see AppView
 */
public enum Screen {

    /** Splash launch screen with animation. */
    SPLASH("Splash", "/com/example/masroofy/App/SplashView.fxml", 500, 750),

    /** PIN entry and authentication screen. */
    PIN("Pin", "/com/example/masroofy/View/PinView.fxml", 500, 750),

    /** First-time application setup and configuration screen. */
    SETUP("Setup", "/com/example/masroofy/View/SetupView.fxml", 500, 750),

    /** Main dashboard showing financial summary and statistics. */
    DASHBOARD("Dashboard", "/com/example/masroofy/View/DashboardView.fxml", 500, 750),

    /** Quick entry screen for adding new income/expense transactions. */
    QUICK_ENTRY("QuickEntry", "/com/example/masroofy/View/QuickEntry.fxml", 500, 750),

    /** Transaction history viewer with filtering and editing capabilities. */
    HISTORY("History", "/com/example/masroofy/View/HistoryView.fxml", 500, 750),

    /** Application settings and user preferences screen. */
    SETTINGS("Settings", "/com/example/masroofy/View/SettingsView.fxml", 500, 750);

    /** The display name of the screen. */
    private final String name;

    /** The resource path to the FXML file for this screen. */
    private final String fxmlPath;

    /** The width of the screen in pixels. */
    private final double width;

    /** The height of the screen in pixels. */
    private final double height;

    /**
     * Constructs a Screen enum constant with the specified properties.
     *
     * @param name the display name of the screen
     * @param fxmlPath the resource path to the FXML file
     * @param width the width of the screen in pixels
     * @param height the height of the screen in pixels
     */
    Screen(String name, String fxmlPath, double width, double height) {
        this.name = name;
        this.fxmlPath = fxmlPath;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the display name of the screen.
     *
     * @return the screen's display name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the resource path to the FXML file for this screen.
     *
     * @return the FXML file path
     */
    public String getFxmlPath() {
        return fxmlPath;
    }

    /**
     * Returns the width of the screen in pixels.
     *
     * @return the screen width
     */
    public double getWidth() {
        return width;
    }

    /**
     * Returns the height of the screen in pixels.
     *
     * @return the screen height
     */
    public double getHeight() {
        return height;
    }
}