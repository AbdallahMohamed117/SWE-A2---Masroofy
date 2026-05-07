package com.example.masroofy.App;

import com.example.masroofy.Controller.*;
import com.example.masroofy.Model.*;
import com.example.masroofy.Model.Entity.Transaction;
import com.example.masroofy.View.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * Main controller that orchestrates the entire application flow and screen navigation.
 * <p>
 * The {@code AppController} acts as the central coordinator in the MVC architecture.
 * It manages the application model, view, and handles transitions between all screens
 * including Splash, PIN, Setup, Dashboard, Quick Entry, History, and Settings.
 * </p>
 *
 * <p>The controller follows a lazy-loading pattern where views are created only when
 * first needed, and provides navigation methods that switch between screens using
 * the {@link AppView} component.</p>
 *
 * <p><b>Navigation Flow:</b></p>
 * <ol>
 *   <li>Splash screen → checks if PIN exists → PIN or Setup</li>
 *   <li>PIN → Dashboard</li>
 *   <li>Setup → PIN</li>
 *   <li>Dashboard → Quick Entry / History / Settings</li>
 *   <li>History → Edit Transaction → Quick Entry</li>
 * </ol>
 *
 * @author Your Name
 * @version 1.0
 * @since 1.0
 * @see AppModel
 * @see AppView
 * @see Screen
 */
public class AppController {

    // Model Components
    /** The application data model containing all business logic and data. */
    private final AppModel model;

    /** The view manager responsible for UI window management. */
    private final AppView appView;

    // Settings Screen Components
    private Parent settingsRoot;
    private SettingsView settingsView;

    // Splash Screen Components
    private Parent splashRoot;
    private SplashView splashView;

    // PIN Screen Components
    private Parent pinRoot;
    private PinView pinView;

    // Setup Screen Components
    private Parent setupRoot;
    private SetupView setupView;

    // Dashboard Screen Components
    private Parent dashboardRoot;
    private DashboardView dashboardView;
    private DashboardController dashboardController;

    // Quick Entry Screen Components
    private Parent quickEntryRoot;
    private QuickEntryView quickEntryView;

    // History Screen Components
    private Parent historyRoot;
    private HistoryView historyView;
    private HistoryController historyController;

    /** The transaction being edited, or {@code null} if not in edit mode. */
    private Transaction editTransaction;

    /**
     * Constructs an {@code AppController} with the specified primary stage.
     * <p>
     * Initializes the application view and model. The splash screen will be shown
     * immediately after construction when {@link #navigateTo(Screen)} is called
     * with {@link Screen#SPLASH}.
     * </p>
     *
     * @param primaryStage the main application window (stage)
     * @throws NullPointerException if {@code primaryStage} is {@code null}
     */
    public AppController(Stage primaryStage) {
        if (primaryStage == null) {
            throw new NullPointerException("primaryStage cannot be null");
        }
        appView = new AppView(primaryStage);
        model = new AppModel();
    }

    /**
     * Navigates to the specified screen.
     * <p>
     * This is the main entry point for screen transitions. The method delegates
     * to the appropriate private show method based on the screen parameter.
     * </p>
     *
     * @param screen the target screen to navigate to
     * @throws NullPointerException if {@code screen} is {@code null}
     * @see Screen
     */
    public void navigateTo(Screen screen) {
        if (screen == null) {
            throw new NullPointerException("screen cannot be null");
        }

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

    /**
     * Displays the Settings screen.
     * <p>
     * Loads the Settings view via FXML if not already loaded, initializes the
     * SettingsController with the PIN and Setup models, and sets up navigation
     * callbacks for returning to Dashboard or resetting to Setup.
     * </p>
     */
    private void showSettings() {
        if (settingsView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.SETTINGS.getFxmlPath()));
                settingsRoot = loader.load();
                settingsView = loader.getController();
                new SettingsController(model.getPin(), model.getSetup(), settingsView);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        settingsView.setOnNavigateBack(() -> navigateTo(Screen.DASHBOARD));
        settingsView.setOnNavigateToSetup(() -> {
            resetAllViews();
            navigateTo(Screen.SETUP);
        });
        appView.switchTo(settingsRoot, Screen.SETTINGS);
    }

    /**
     * Resets all view references to {@code null}.
     * <p>
     * This method is called when navigating from Settings to Setup to ensure
     * that all views are recreated fresh, preventing stale state issues.
     * </p>
     */
    private void resetAllViews() {
        settingsRoot = null;
        settingsView = null;
        dashboardRoot = null;
        dashboardView = null;
        dashboardController = null;
        quickEntryRoot = null;
        quickEntryView = null;
        historyRoot = null;
        historyView = null;
        historyController = null;
        editTransaction = null;
    }

    /**
     * Displays the Splash screen.
     * <p>
     * Creates a new {@link SplashView} and starts its animation. When the animation
     * completes, the application navigates to either the PIN screen (if the user
     * has set a PIN and is active) or the Setup screen (for first-time users).
     * </p>
     */
    private void showSplash() {
        if (splashView == null) {
            splashView = new SplashView();
            splashRoot = splashView.createRoot();
            splashView.startAnimation(() -> {
                if (model.hasPin() && model.getPin().isUserActive()) {
                    navigateTo(Screen.PIN);
                } else {
                    navigateTo(Screen.SETUP);
                }
            });
        }
        appView.switchTo(splashRoot, Screen.SPLASH);
    }

    /**
     * Displays the PIN entry screen.
     * <p>
     * Loads the PIN view via FXML if not already loaded, creates a {@link PinController}
     * with the PIN model, and sets up a callback to navigate to Dashboard upon
     * successful PIN entry.
     * </p>
     */
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

    /**
     * Displays the Setup (first-time configuration) screen.
     * <p>
     * Loads the Setup view via FXML if not already loaded, creates a {@link SetupController}
     * with the Setup model, and sets up navigation to the PIN screen upon completion.
     * </p>
     */
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
        setupView.setOnNavigateToDashboard(() -> navigateTo(Screen.PIN));
        appView.switchTo(setupRoot, Screen.SETUP);
    }

    /**
     * Displays the Dashboard screen.
     * <p>
     * Loads the Dashboard view via FXML if not already loaded, creates a
     * {@link DashboardController} with the Dashboard model, and sets up navigation
     * callbacks for Quick Entry, History, and Settings screens. The dashboard
     * data is refreshed each time the screen is shown.
     * </p>
     */
    private void showDashboard() {
        if (dashboardView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.DASHBOARD.getFxmlPath()));
                dashboardRoot = loader.load();
                dashboardView = loader.getController();
                Dashboard dashboardModel = model.getDashboard();
                dashboardController = new DashboardController(dashboardModel, dashboardView);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        dashboardView.setOnNavigateToQuickEntry(() -> navigateTo(Screen.QUICK_ENTRY));
        dashboardView.setOnNavigateToHistory(() -> navigateTo(Screen.HISTORY));
        dashboardView.setOnNavigateToSettings(() -> navigateTo(Screen.SETTINGS));
        dashboardController.refreshDashboard();
        appView.switchTo(dashboardRoot, Screen.DASHBOARD);
    }

    /**
     * Displays the Quick Entry screen for adding or editing transactions.
     * <p>
     * This method supports two modes:
     * <ul>
     *   <li><b>Add mode:</b> When {@code editTransaction} is {@code null}, creates a
     *       new {@link QuickEntryController} with a fresh QuickEntry model.</li>
     *   <li><b>Edit mode:</b> When {@code editTransaction} is not {@code null}, creates
     *       a {@link QuickEntryEditController} to edit the existing transaction.
     *       After editing, navigates back to History screen.</li>
     * </ul>
     * </p>
     */
    private void showQuickEntry() {
        try {
            if (quickEntryView == null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.QUICK_ENTRY.getFxmlPath()));
                quickEntryRoot = loader.load();
                quickEntryView = loader.getController();
            }

            if (editTransaction != null) {
                // Edit mode
                History historyModel = model.getHistory();
                new QuickEntryEditController(historyModel, quickEntryView, editTransaction, () -> {
                    editTransaction = null;
                    navigateTo(Screen.HISTORY);
                });
                quickEntryView.setOnNavigateBack(() -> {
                    editTransaction = null;
                    navigateTo(Screen.HISTORY);
                });
            } else {
                // Add mode
                QuickEntry quickEntryModel = model.getQuickEntry();
                QuickEntryController qec = new QuickEntryController(quickEntryModel, quickEntryView);
                qec.PrintView();
                quickEntryView.setOnNavigateBack(() -> navigateTo(Screen.DASHBOARD));
            }

            appView.switchTo(quickEntryRoot, Screen.QUICK_ENTRY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the Transaction History screen.
     * <p>
     * Loads the History view via FXML if not already loaded, creates a
     * {@link HistoryController} with the History model, and sets up callbacks for:
     * <ul>
     *   <li>Navigating back to Dashboard</li>
     *   <li>Editing a transaction (which navigates to Quick Entry in edit mode)</li>
     * </ul>
     * The transaction list is refreshed each time the screen is shown.
     * </p>
     */
    private void showHistory() {
        if (historyView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Screen.HISTORY.getFxmlPath()));
                historyRoot = loader.load();
                historyView = loader.getController();
                History historyModel = model.getHistory();
                historyController = new HistoryController(historyModel, historyView);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        historyController.refreshHistory();
        historyView.setOnNavigateBack(() -> navigateTo(Screen.DASHBOARD));
        historyView.setOnNavigateToEdit(transaction -> {
            editTransaction = transaction;
            navigateTo(Screen.QUICK_ENTRY);
        });
        appView.switchTo(historyRoot, Screen.HISTORY);
    }
}