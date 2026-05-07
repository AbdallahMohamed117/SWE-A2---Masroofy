package com.example.masroofy.Controller;

/**
 * Base interface for all controllers in the Masroofy application.
 * <p>
 * The {@code AbstractController} interface defines the contract that all
 * controller classes must implement. It provides a standard method for
 * initializing or refreshing the view associated with each controller.
 * </p>
 *
 * <p>Controllers that implement this interface typically:</p>
 * <ul>
 *   <li>Handle user input and interaction logic</li>
 *   <li>Coordinate between the model and view layers</li>
 *   <li>Update the UI with data from the model</li>
 * </ul>
 *
 * <p><b>Implementing classes include:</b></p>
 * <ul>
 *   <li>{@link DashboardController}</li>
 *   <li>{@link HistoryController}</li>
 *   <li>{@link PinController}</li>
 *   <li>{@link QuickEntryController}</li>
 *   <li>{@link QuickEntryEditController}</li>
 *   <li>{@link SetupController}</li>
 *   <li>{@link SettingsController}</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see DashboardController
 * @see HistoryController
 * @see QuickEntryController
 */
public interface AbstractController {

    /**
     * Displays or refreshes the view associated with this controller.
     * <p>
     * This method is responsible for rendering the current state of the model
     * to the user interface. It is typically called when:
     * </p>
     * <ul>
     *   <li>The controller is first initialized</li>
     *   <li>Data in the underlying model has changed</li>
     *   <li>The view needs to be refreshed after user actions</li>
     * </ul>
     * <p>
     * The exact behavior of this method depends on the specific controller
     * implementation. For example:
     * </p>
     * <ul>
     *   <li>{@code DashboardController} updates financial summaries and charts</li>
     *   <li>{@code HistoryController} refreshes the transaction list</li>
     *   <li>{@code QuickEntryController} resets form fields or updates dropdowns</li>
     * </ul>
     *
     * @see DashboardController#printView()
     * @see HistoryController#refreshHistory()
     */
    void printView();
}