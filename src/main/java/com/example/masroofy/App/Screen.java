package com.example.masroofy.App;

public enum Screen {
    SPLASH("Splash", "/com/example/masroofy/App/SplashView.fxml", 400, 650),
    PIN("Pin", "/com/example/masroofy/View/PinView.fxml", 400, 650),
    SETUP("Setup", "/com/example/masroofy/View/SetupView.fxml", 400, 700),
    DASHBOARD("Dashboard", "/com/example/masroofy/View/DashboardView.fxml", 380, 700),
    QUICK_ENTRY("QuickEntry", "/com/example/masroofy/View/QuickEntry.fxml", 400, 700),
    HISTORY("History", "/com/example/masroofy/View/HistoryView.fxml", 500, 750);

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