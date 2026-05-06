package com.example.masroofy.App;

public enum Screen {
    SPLASH("Splash", "/com/example/masroofy/App/SplashView.fxml", 500, 750),
    PIN("Pin", "/com/example/masroofy/View/PinView.fxml", 500, 750),
    SETUP("Setup", "/com/example/masroofy/View/SetupView.fxml", 500, 750),
    DASHBOARD("Dashboard", "/com/example/masroofy/View/DashboardView.fxml", 500, 750),
    QUICK_ENTRY("QuickEntry", "/com/example/masroofy/View/QuickEntry.fxml", 500, 750),
    HISTORY("History", "/com/example/masroofy/View/HistoryView.fxml", 500, 750),
    SETTINGS("Settings", "/com/example/masroofy/View/SettingsView.fxml", 500, 750);

    private final String name;
    private final String fxmlPath;
    private final double width;
    private final double height;

    Screen(String name, String fxmlPath, double width, double height) {
        this.name = name;
        this.fxmlPath = fxmlPath;
        this.width = width;
        this.height = height;
    }

    public String getName() { return name; }
    public String getFxmlPath() { return fxmlPath; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}
