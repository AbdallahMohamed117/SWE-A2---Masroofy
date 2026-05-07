package com.example.masroofy.View;

/**
 * Base interface for all view classes in the Masroofy application.
 * <p>
 * The {@code AbstractView} interface defines the contract that all view classes
 * must implement. It provides a standard method for rendering or refreshing
 * the user interface components of each screen.
 * </p>
 *
 * <p><b>Purpose:</b></p>
 * <ul>
 *   <li>Ensures consistent view initialization across all screens</li>
 *   <li>Provides a common method for refreshing UI components</li>
 *   <li>Enables polymorphic handling of different view types</li>
 * </ul>
 *
 * <p><b>Implementing Classes:</b></p>
 * <ul>
 *   <li>{@link DashboardView} - Main dashboard with financial summaries</li>
 *   <li>{@link HistoryView} - Transaction history with filters</li>
 *   <li>{@link PinView} - PIN entry and authentication screen</li>
 *   <li>{@link QuickEntryView} - Quick expense entry form</li>
 *   <li>{@link SetupView} - First-time budget setup screen</li>
 *   <li>{@link SettingsView} - Application settings and preferences</li>
 *   <li>{@link SplashView} - Launch splash screen (uses createRoot instead)</li>
 * </ul>
 *
 * <p><b>Usage Pattern:</b></p>
 * <pre>
 * // In a controller
 * public class SomeController implements AbstractController {
 *     private SomeView view;
 *
 *     public void refreshDisplay() {
 *         // Update view data
 *         view.setData(someData);
 *         // Refresh the screen
 *         view.printScreen();
 *     }
 * }
 * </pre>
 *
 * @version 1.0
 * @since 1.0
 * @see DashboardView
 * @see HistoryView
 * @see PinView
 * @see QuickEntryView
 * @see SetupView
 * @see SettingsView
 * @see com.example.masroofy.Controller.AbstractController
 */
public interface AbstractView {

    /**
     * Renders or refreshes the user interface components of the view.
     * <p>
     * This method is responsible for updating all UI elements to reflect
     * the current state of the underlying model. It is typically called:
     * </p>
     * <ul>
     *   <li>After the view is first created</li>
     *   <li>When data in the model has changed</li>
     *   <li>After a user action that requires UI updates</li>
     *   <li>When navigating back to a screen that may have stale data</li>
     * </ul>
     *
     * <p><b>Implementation Guidelines:</b></p>
     * <ul>
     *   <li>Should update all dynamic UI elements (labels, tables, charts)</li>
     *   <li>Should be lightweight enough to call frequently</li>
     *   <li>Should not perform expensive database operations directly</li>
     *   <li>Should assume data has already been loaded by the controller</li>
     * </ul>
     *
     * <p><b>Example Implementation:</b></p>
     * <pre>
     * &#64;Override
     * public void printScreen() {
     *     totalSpentLabel.setText(String.format("%.2f", totalSpent));
     *     progressBar.setProgress(progress);
     *     pieChart.setData(pieChartData);
     * }
     * </pre>
     *
     * @see com.example.masroofy.Controller.AbstractController#printView()
     */
    void printScreen();
}