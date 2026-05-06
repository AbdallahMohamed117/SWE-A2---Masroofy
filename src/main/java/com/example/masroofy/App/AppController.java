package com.example.masroofy.App;

import com.example.masroofy.Controller.*;
import com.example.masroofy.Model.*;
import com.example.masroofy.View.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class AppController {
    private final AppModel model;
    private final AppView appView;
    private Parent settingsRoot;
    private SettingsView settingsView;

    private Parent splashRoot;
    private SplashView splashView;
    private Parent pinRoot;
    private PinView pinView;
    private Parent setupRoot;
    private SetupView setupView;
    private Parent dashboardRoot;
    private DashboardView dashboardView;
    private Parent quickEntryRoot;
    private QuickEntryView quickEntryView;
    private Parent historyRoot;
    private HistoryView historyView;

    public AppController(Stage primaryStage) {
        appView = new AppView(primaryStage);
        model = new AppModel();
    }

    public void navigateTo(Screen screen) {
        switch (screen) {
            case SPLASH: showSplash(); break;
            case PIN: showPin(); break;
            case SETUP: showSetup(); break;
            case DASHBOARD: showDashboard(); break;
            case QUICK_ENTRY: showQuickEntry(); break;
            case HISTORY: showHistory(); break;
            case SETTINGS: showSettings(); break;
        }
    }
    private void showSettings() {
        if (settingsView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.SETTINGS.getFxmlPath()));
                settingsRoot = loader.load();
                settingsView = loader.getController();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        settingsView.setOnNavigateBack(() -> navigateTo(Screen.DASHBOARD));
        settingsView.setOnNavigateToSetup(() -> navigateTo(Screen.SETUP));
        appView.switchTo(settingsRoot, Screen.SETTINGS);
    }
    private void showSplash() {
        if (splashView == null) {
            splashView = new SplashView();
            splashRoot = splashView.createRoot();
            splashView.startAnimation(() -> {
                if (model.hasPin()) {
                    navigateTo(Screen.PIN);
                } else {
                    navigateTo(Screen.SETUP);
                }
            });
        }
        appView.switchTo(splashRoot, Screen.SPLASH);
    }

    private void showPin() {
        if (pinView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.PIN.getFxmlPath()));
                pinRoot = loader.load();
                pinView = loader.getController();
                Pin pinModel = model.getPin();
                new PinController(pinModel, pinView);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        pinView.setOnNavigateToDashboard(() -> navigateTo(Screen.DASHBOARD));
        appView.switchTo(pinRoot, Screen.PIN);
    }

    private void showSetup() {
        if (setupView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.SETUP.getFxmlPath()));
                setupRoot = loader.load();
                setupView = loader.getController();
                Setup setupModel = model.getSetup();
                new SetupController(setupModel, setupView);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        setupView.setOnNavigateToDashboard(() -> navigateTo(Screen.DASHBOARD));
        appView.switchTo(setupRoot, Screen.SETUP);
    }

    private void showDashboard() {
        if (dashboardView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.DASHBOARD.getFxmlPath()));
                dashboardRoot = loader.load();
                dashboardView = loader.getController();
                Dashboard dashboardModel = model.getDashboard();
                new DashboardController(dashboardModel, dashboardView);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        dashboardView.setOnNavigateToQuickEntry(() -> navigateTo(Screen.QUICK_ENTRY));
        dashboardView.setOnNavigateToHistory(() -> navigateTo(Screen.HISTORY));
        dashboardView.setOnNavigateToSettings(() -> navigateTo(Screen.SETTINGS));
        appView.switchTo(dashboardRoot, Screen.DASHBOARD);

    }

    private void showQuickEntry() {
        if (quickEntryView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.QUICK_ENTRY.getFxmlPath()));
                quickEntryRoot = loader.load();
                quickEntryView = loader.getController();
                QuickEntry quickEntryModel = model.getQuickEntry();
                QuickEntryController qec = new QuickEntryController(quickEntryModel, quickEntryView);
                qec.PrintView();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        quickEntryView.setOnNavigateBack(() -> navigateTo(Screen.DASHBOARD));
        appView.switchTo(quickEntryRoot, Screen.QUICK_ENTRY);
    }

    private void showHistory() {
        if (historyView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.HISTORY.getFxmlPath()));
                historyRoot = loader.load();
                historyView = loader.getController();
                History historyModel = model.getHistory();
                new HistoryController(historyModel, historyView);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        historyView.setOnNavigateBack(() -> navigateTo(Screen.DASHBOARD));
        appView.switchTo(historyRoot, Screen.HISTORY);
    }
}
