package com.example.masroofy.View;

import com.example.masroofy.Listener.HistoryListener;
import com.example.masroofy.Model.Entity.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * View class for the Transaction History screen in the Masroofy application.
 * <p>
 * The {@code HistoryView} manages the display of transaction history with
 * filtering capabilities by category and date range. It provides a list view
 * of transactions, each with edit and delete buttons, and displays summary
 * information including total expenses and transaction count.
 * </p>
 *
 * <p><b>UI Components:</b></p>
 * <ul>
 *   <li>Transaction list with category emojis and amount display</li>
 *   <li>Category filter ComboBox with all available categories</li>
 *   <li>Date filter ComboBox (Last Day, Last Week, Last Month)</li>
 *   <li>Apply Filter and Clear Filter buttons</li>
 *   <li>Total expenses and transaction count summary labels</li>
 *   <li>Edit and Delete buttons for each transaction</li>
 * </ul>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Custom styled ComboBoxes with prompt text</li>
 *   <li>Category-based emoji mapping for visual recognition</li>
 *   <li>Delete confirmation dialog before removal</li>
 *   <li>Update success confirmation dialog</li>
 *   <li>Empty state display when no transactions exist</li>
 * </ul>
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractView
 * @see HistoryListener
 * @see com.example.masroofy.Controller.HistoryController
 * @see Transaction
 */
public class HistoryView implements AbstractView {

    // FXML Injected Components
    @FXML private Label totalExpensesLabel;
    @FXML private Label transactionCountLabel;
    @FXML private VBox transactionListVBox;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> dateComboBox;
    @FXML private Button btnApplyFilter;
    @FXML private Button btnClearFilter;

    private HistoryListener listener;
    private Runnable onNavigateBack;
    private Consumer<Transaction> onNavigateToEdit;

    /**
     * Sets the callback for navigating to edit a transaction.
     *
     * @param callback the Consumer that accepts a Transaction to edit
     */
    public void setOnNavigateToEdit(Consumer<Transaction> callback) {
        this.onNavigateToEdit = callback;
    }

    /**
     * Sets the callback for navigating back to the previous screen.
     *
     * @param r the Runnable to execute when back navigation is triggered
     */
    public void setOnNavigateBack(Runnable r) {
        this.onNavigateBack = r;
    }

    /**
     * Sets the HistoryListener for handling user interactions.
     *
     * @param l the HistoryListener to set
     */
    public void setListener(HistoryListener l) {
        listener = l;
    }

    /**
     * This method is intentionally empty as the History view does not require
     * a full screen refresh. Individual transactions are updated via specific methods.
     */
    @Override
    public void printScreen() {
        // Screen updates are handled by showTransactions() method
    }

    /**
     * Initializes the view after FXML loading.
     * <p>
     * Styles the category and date ComboBoxes with custom prompt text.
     * </p>
     */
    @FXML
    public void initialize() {
        styleComboBox(categoryComboBox, "Category");
        styleComboBox(dateComboBox, "Date");
    }

    /**
     * Populates the category ComboBox with the provided list of categories.
     *
     * @param categories the list of category names to display
     */
    public void setComboBox(List<String> categories) {
        categoryComboBox.getItems().clear();
        categoryComboBox.getItems().addAll(categories);
    }

    /**
     * Populates the date filter ComboBox with the provided date options.
     *
     * @param dates the list of date filter options (e.g., "Last Day", "Last Week", "Last Month")
     */
    public void setDateFilters(List<String> dates) {
        dateComboBox.getItems().clear();
        dateComboBox.getItems().addAll(dates);
    }

    /**
     * Applies custom styling to a ComboBox with a prompt text.
     *
     * @param comboBox the ComboBox to style
     * @param prompt the prompt text to display when no selection is made
     */
    private void styleComboBox(ComboBox<String> comboBox, String prompt) {
        comboBox.setButtonCell(createPromptCell(prompt));
        comboBox.setCellFactory(listView -> createDropdownCell());
    }

    /**
     * Creates a custom ListCell for the ComboBox button area with prompt text.
     *
     * @param prompt the prompt text to display
     * @return a styled ListCell for the button area
     */
    private ListCell<String> createPromptCell(String prompt) {
        return new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(prompt);
                    setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 13;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-font-size: 13;");
                }
            }
        };
    }

    /**
     * Creates a custom ListCell for ComboBox dropdown items.
     *
     * @return a styled ListCell for dropdown items
     */
    private ListCell<String> createDropdownCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setBackground(null);
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: white; -fx-font-size: 13;");
                    setBackground(javafx.scene.layout.Background.fill(
                            javafx.scene.paint.Color.TRANSPARENT));
                }
            }
        };
    }

    /**
     * Displays the list of transactions in the UI.
     * <p>
     * Clears the existing transaction list, builds rows for each transaction,
     * and updates the total expenses and transaction count labels.
     * </p>
     *
     * @param list the list of transactions to display
     */
    public void showTransactions(List<Transaction> list) {
        transactionListVBox.getChildren().clear();

        if (list == null || list.isEmpty()) {
            showEmptyState();
            totalExpensesLabel.setText("EGP " + String.format("%.2f", 0.0));
            transactionCountLabel.setText(String.valueOf(0));
            return;
        }

        double total = 0;
        for (Transaction t : list) {
            transactionListVBox.getChildren().add(buildTransactionRow(t));
            total += t.getTransactionAmount();
        }

        totalExpensesLabel.setText("EGP " + String.format("%.2f", total));
        transactionCountLabel.setText(String.valueOf(list.size()));
    }

    /**
     * Builds a visual row for a single transaction.
     * <p>
     * Creates an HBox containing the category emoji icon, transaction details,
     * amount, and edit/delete buttons.
     * </p>
     *
     * @param t the transaction to display
     * @return an HBox representing the transaction row
     */
    private HBox buildTransactionRow(Transaction t) {
        HBox row = new HBox(15);
        row.setStyle(
                "-fx-background-color: rgba(255,255,255,0.03);" +
                        "-fx-padding: 12; -fx-background-radius: 15;"
        );

        String emoji = getCategoryEmoji(t.getTransactionCategory().getCategoryName());
        Label icon = new Label(emoji);
        icon.setStyle(
                "-fx-background-color: rgba(45,212,191,0.1);" +
                        "-fx-padding: 10; -fx-background-radius: 12;"
        );

        VBox info = new VBox(3);
        Label nameLabel = new Label(t.getTransactionCategory().getCategoryName());
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        Label categoryLabel = new Label(t.getTransactionCategory().getCategoryName());
        categoryLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 11;");
        info.getChildren().addAll(nameLabel, categoryLabel);

        HBox.setHgrow(info, Priority.ALWAYS);

        VBox amountBox = new VBox(5);
        amountBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        Label amountLabel = new Label("- EGP " + String.format("%.2f", t.getTransactionAmount()));
        amountLabel.setStyle("-fx-text-fill: #fb7185; -fx-font-weight: bold;");

        Button editBtn = new Button("Edit");
        editBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #2dd4bf;" +
                        "-fx-font-size: 10; -fx-padding: 0; -fx-cursor: hand; -fx-underline: true;"
        );
        editBtn.setOnAction(e -> showEditForm(t));

        Button deleteBtn = new Button("Delete");
        deleteBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #fb7185;" +
                        "-fx-font-size: 10; -fx-padding: 0; -fx-cursor: hand; -fx-underline: true;"
        );
        deleteBtn.setOnAction(e -> {
            if (showDeleteConfirmation()) {
                if (listener != null) {
                    listener.onDeleteClicked(t);
                }
            }
        });

        HBox buttonRow = new HBox(10);
        buttonRow.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        buttonRow.getChildren().addAll(editBtn, deleteBtn);
        amountBox.getChildren().addAll(amountLabel, buttonRow);

        row.getChildren().addAll(icon, info, amountBox);

        return row;
    }

    /**
     * Returns an emoji icon for a given category name.
     *
     * @param category the category name
     * @return the corresponding emoji character, or a default money bag emoji
     */
    private String getCategoryEmoji(String category) {
        if (category == null) return "\ud83d\udcb0";
        switch (category.toLowerCase()) {
            case "food": return "\ud83c\udf54";
            case "transport": return "\ud83d\ude97";
            case "entertainment": return "\ud83c\udfae";
            default: return "\ud83d\udcb0";
        }
    }

    /**
     * Shows the edit form for a transaction.
     * <p>
     * First attempts to use the onNavigateToEdit callback (preferred), then falls back
     * to the legacy listener method.
     * </p>
     *
     * @param transaction the transaction to edit
     */
    public void showEditForm(Transaction transaction) {
        if (onNavigateToEdit != null) {
            onNavigateToEdit.accept(transaction);
        } else if (listener != null) {
            listener.onEditClicked(transaction);
        }
    }

    /**
     * Calculates the date range based on the selected filter option.
     *
     * @param selection the selected filter string ("Last Day", "Last Week", "Last Month")
     * @return an array containing [fromDate, toDate] where fromDate may be null for no lower bound
     */
    private Date[] getDateRangeFromSelection(String selection) {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        Date from = null;

        if ("Last Day".equals(selection)) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            from = cal.getTime();
        } else if ("Last Week".equals(selection)) {
            cal.add(Calendar.DAY_OF_MONTH, -7);
            from = cal.getTime();
        } else if ("Last Month".equals(selection)) {
            cal.add(Calendar.MONTH, -1);
            from = cal.getTime();
        }

        return new Date[] { from, now };
    }

    /**
     * Displays an empty state message when no transactions exist.
     */
    public void showEmptyState() {
        Label emptyLabel = new Label("No Transactions Found");
        emptyLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 14;");
        transactionListVBox.getChildren().add(emptyLabel);
    }

    /**
     * Displays a message when no transactions match the applied filters.
     */
    public void showNoFilterResults() {
        transactionListVBox.getChildren().clear();
        Label noResults = new Label("No transactions found for the selected filter.");
        noResults.setStyle("-fx-text-fill: #64748b; -fx-font-size: 13;");
        transactionListVBox.getChildren().add(noResults);
    }

    /**
     * Shows a confirmation dialog before deleting a transaction.
     *
     * @return {@code true} if the user confirms deletion, {@code false} otherwise
     */
    public boolean showDeleteConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to delete this transaction?");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }

    /**
     * Shows a confirmation dialog when a transaction is successfully updated.
     */
    public void showUpdateConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Transaction Updated");
        alert.showAndWait();
    }

    /**
     * Event handler for the back button click.
     * <p>
     * Checks with the listener if back navigation is allowed before proceeding.
     * </p>
     */
    @FXML
    public void onBackClicked() {
        if (listener != null && listener.onBackClicked()) {
            if (onNavigateBack != null) onNavigateBack.run();
        }
    }

    /**
     * Event handler for the Apply Filter button click.
     * <p>
     * Retrieves the selected category and date range, then notifies the listener
     * to apply the filters.
     * </p>
     */
    @FXML
    private void onApplyFilterClicked() {
        String category = categoryComboBox.getValue();
        Date[] dateRange = getDateRangeFromSelection(dateComboBox.getValue());
        listener.onFilterApplied(category, dateRange[0], dateRange[1]);
    }

    /**
     * Event handler for the Clear Filter button click.
     * <p>
     * Clears the selection from both ComboBoxes and notifies the listener
     * to clear all filters.
     * </p>
     */
    @FXML
    private void onClearFilterClicked() {
        categoryComboBox.getSelectionModel().clearSelection();
        dateComboBox.getSelectionModel().clearSelection();
        if (listener != null) {
            listener.onFilterCleared();
        }
    }
}