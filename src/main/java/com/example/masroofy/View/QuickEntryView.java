package com.example.masroofy.View;

import com.example.masroofy.Listener.QuickEntryListener;
import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

/**
 * View class for the Quick Entry screen in the Masroofy application.
 * <p>
 * The {@code QuickEntryView} manages the expense entry UI, allowing users to
 * quickly add new expenses by entering an amount and selecting a category.
 * It supports both add mode and edit mode for modifying existing transactions.
 * </p>
 *
 * <p><b>UI Components:</b></p>
 * <ul>
 *   <li>Amount input TextField for entering expense amount</li>
 *   <li>Grid of category tiles with color coding</li>
 *   <li>Add Category field for creating new categories</li>
 *   <li>Submit button (text changes between "CONFIRM EXPENSE" and "UPDATE EXPENSE")</li>
 * </ul>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Color-coded category tiles with 12 pre-defined colors</li>
 *   <li>Interactive tile selection with visual feedback (hover, selected states)</li>
 *   <li>Edit mode support for updating existing transactions</li>
 *   <li>Dynamic category addition</li>
 *   <li>Error and success dialog alerts</li>
 * </ul>
 *
 * <p><b>Category Colors:</b></p>
 * The first 12 categories automatically receive distinct colors from a predefined palette.
 *
 * @version 1.0
 * @since 1.0
 * @see AbstractView
 * @see QuickEntryListener
 * @see com.example.masroofy.Controller.QuickEntryController
 * @see com.example.masroofy.Controller.QuickEntryEditController
 */
public class QuickEntryView implements AbstractView {

    // FXML Injected Components
    @FXML private TextField etAmountInput;
    @FXML private Button btnSubmitExpense;
    @FXML private GridPane categoryGrid;
    @FXML private TextField newCategoryField;

    /** Predefined color palette for category tiles (light variants). */
    private static final String[] CATEGORY_COLORS = {
            "#ef4444", "#f97316", "#eab308", "#22c55e",
            "#14b8a6", "#3b82f6", "#8b5cf6", "#ec4899",
            "#06b6d4", "#84cc16", "#f43f5e", "#a855f7"
    };

    /** Predefined color palette for category tiles (dark variants). */
    private static final String[] CATEGORY_COLORS_DARK = {
            "#b91c1c", "#c2410c", "#a16207", "#15803d",
            "#0f766e", "#1d4ed8", "#6d28d9", "#be185d",
            "#0e7490", "#4d7c0f", "#be123c", "#7e22ce"
    };

    private String selectedCategory = null;
    private VBox currentlySelectedTile = null;
    private String currentlySelectedColor = null;
    private QuickEntryListener listener;
    private Runnable onNavigateBack;

    private boolean isEditMode = false;
    private Transaction editingTransaction;

    /**
     * Sets the callback for navigating back to the previous screen.
     *
     * @param r the Runnable to execute when back navigation is triggered
     */
    public void setOnNavigateBack(Runnable r) {
        this.onNavigateBack = r;
    }

    /**
     * Sets the QuickEntryListener for handling user interactions.
     *
     * @param listener the QuickEntryListener to set
     */
    public void setListener(QuickEntryListener listener) {
        this.listener = listener;
    }

    /**
     * Returns the amount entered in the amount text field.
     *
     * @return the amount text as a String
     */
    public String getAmountText() {
        return etAmountInput.getText();
    }

    /**
     * Returns the currently selected category name.
     *
     * @return the selected category name, or {@code null} if none selected
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * Returns the text entered in the new category field.
     *
     * @return the new category text
     */
    public String getNewCategoryText() {
        return newCategoryField.getText();
    }

    /**
     * Resets the view to its initial state.
     * <p>
     * Clears all input fields, deselects any selected category, and exits edit mode.
     * </p>
     */
    @Override
    public void printScreen() {
        exitEditMode();
        etAmountInput.clear();
        newCategoryField.clear();
        selectedCategory = null;
        currentlySelectedTile = null;
    }

    /**
     * Displays the list of categories as color-coded tiles in a grid.
     *
     * @param categories the list of category names to display
     */
    public void showCategories(List<String> categories) {
        if (categoryGrid == null) return;
        categoryGrid.getChildren().clear();
        int col = 0, row = 0;
        for (int i = 0; i < categories.size(); i++) {
            String color = CATEGORY_COLORS[i % CATEGORY_COLORS.length];
            VBox tile = buildCategoryTile(categories.get(i), color);
            categoryGrid.add(tile, col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    /**
     * Displays categories from a list of Category objects.
     *
     * @param categories the list of Category objects to display
     */
    public void showCategoriesFromCategoryList(List<Category> categories) {
        List<String> names = new ArrayList<>();
        for (Category c : categories) names.add(c.getCategoryName());
        showCategories(names);
    }

    /**
     * Builds a single category tile (VBox) with the given name and color.
     *
     * @param categoryName the name of the category
     * @param color the background color for the tile
     * @return a VBox representing a selectable category tile
     */
    private VBox buildCategoryTile(String categoryName, String color) {
        VBox tile = new VBox(8);
        tile.setAlignment(Pos.CENTER);
        String originalStyle = "-fx-background-color: " + color + "; -fx-padding: 12; " +
                "-fx-background-radius: 12; -fx-cursor: hand;";

        tile.setStyle(originalStyle);

        Label label = new Label(categoryName);
        label.setFont(Font.font("System", 14));
        label.setTextFill(Color.WHITE);
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);

        tile.getChildren().add(label);

        // Hover effect
        tile.setOnMouseEntered(e -> {
            if (tile != currentlySelectedTile) {
                tile.setStyle(
                        "-fx-background-color: rgba(14, 165, 233, 0.25);" +
                                "-fx-padding: 12; -fx-background-radius: 12; -fx-cursor: hand;" +
                                "-fx-border-color: rgba(14, 165, 233, 0.5); -fx-border-radius: 12;" +
                                "-fx-translate-y: -2;"
                );
            }
        });

        tile.setOnMouseExited(e -> {
            if (tile != currentlySelectedTile) {
                tile.setStyle(originalStyle);
            }
        });

        tile.setOnMouseClicked(e -> selectCategory(categoryName, tile, color));
        return tile;
    }

    /**
     * Selects a category tile, updating visual styles and tracking the selection.
     *
     * @param categoryName the name of the selected category
     * @param tile the VBox tile that was clicked
     * @param color the color of the selected tile
     */
    private void selectCategory(String categoryName, VBox tile, String color) {
        // Deselect previous selection
        if (currentlySelectedTile != null) {
            currentlySelectedTile.setStyle(
                    "-fx-background-color: rgba(14, 165, 233, 0.15);" +
                            "-fx-padding: 12; -fx-background-radius: 12; -fx-cursor: hand;" +
                            "-fx-border-color: rgba(14, 165, 233, 0.3); -fx-border-radius: 12;"
            );
        }

        // Style the newly selected tile
        tile.setStyle(
                "-fx-background-color: " + color + "; -fx-padding: 12; " +
                        "-fx-background-radius: 12; -fx-cursor: hand; " +
                        "-fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 12; -fx-opacity: 0.85;"
        );

        selectedCategory = categoryName;
        currentlySelectedTile = tile;
        currentlySelectedColor = color;
    }

    /**
     * Selects a category tile by its name (used in edit mode).
     *
     * @param categoryName the name of the category to select
     */
    private void selectCategoryByName(String categoryName) {
        if (categoryGrid == null) return;
        for (javafx.scene.Node node : categoryGrid.getChildren()) {
            if (node instanceof VBox) {
                VBox tile = (VBox) node;
                if (!tile.getChildren().isEmpty() && tile.getChildren().get(0) instanceof Label) {
                    Label label = (Label) tile.getChildren().get(0);
                    if (categoryName.equals(label.getText())) {
                        String style = tile.getStyle();
                        String color = "#3b82f6";
                        int start = style.indexOf("-fx-background-color:") + 22;
                        int end = style.indexOf(";", start);
                        if (start > 21 && end > start) color = style.substring(start, end).trim();
                        selectCategory(categoryName, tile, color);
                        return;
                    }
                }
            }
        }
    }

    /**
     * Enters edit mode with the specified transaction.
     * <p>
     * Pre-fills the amount field, selects the transaction's category,
     * and changes the submit button text to "UPDATE EXPENSE".
     * </p>
     *
     * @param transaction the transaction to edit
     */
    public void enterEditMode(Transaction transaction) {
        isEditMode = true;
        editingTransaction = transaction;
        etAmountInput.setText(String.valueOf(transaction.getTransactionAmount()));
        selectCategoryByName(transaction.getTransactionCategory().getCategoryName());
        btnSubmitExpense.setText("UPDATE EXPENSE");
    }

    /**
     * Exits edit mode and resets the view to add mode.
     * <p>
     * Clears the editing transaction reference and changes the submit button
     * text back to "CONFIRM EXPENSE".
     * </p>
     */
    public void exitEditMode() {
        isEditMode = false;
        editingTransaction = null;
        if (btnSubmitExpense != null) btnSubmitExpense.setText("CONFIRM EXPENSE");
    }

    /**
     * Clears the new category text field.
     */
    public void clearNewCategory() {
        if (newCategoryField != null) {
            newCategoryField.clear();
        }
    }

    /**
     * Shows an error message dialog.
     *
     * @param msg the error message to display
     */
    public void showErrorMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Shows a confirmation dialog when an expense is successfully saved.
     */
    public void showSavedConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Expense saved successfully!");
        alert.showAndWait();
    }

    /**
     * Shows a confirmation dialog when a transaction is successfully updated.
     */
    public void showUpdateConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Transaction updated successfully!");
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
     * Event handler for the submit button click.
     * <p>
     * If in edit mode, calls {@code onEditSubmitted}; otherwise calls
     * {@code onSubmitExpense} on the listener.
     * </p>
     */
    @FXML
    private void onSubmitExpense() {
        String amountText = getAmountText();
        String selectedCat = getSelectedCategory();

        if (isEditMode && editingTransaction != null) {
            listener.onEditSubmitted(editingTransaction, amountText, selectedCat);
        } else {
            listener.onSubmitExpense(amountText, selectedCat);
        }
    }

    /**
     * Event handler for the Add Category button click.
     * <p>
     * Notifies the listener to add a new category with the entered name.
     * </p>
     */
    @FXML
    private void onAddCategoryClicked() {
        listener.onAddCategoryClicked(getNewCategoryText());
    }
}