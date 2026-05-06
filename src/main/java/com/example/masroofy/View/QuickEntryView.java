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

public class QuickEntryView implements AbstractView {

    @FXML private TextField etAmountInput;
    @FXML private Button btnSubmitExpense;
    @FXML private GridPane categoryGrid;
    @FXML private TextField newCategoryField;

    private static final String[] CATEGORY_COLORS = {
            "#ef4444", "#f97316", "#eab308", "#22c55e",
            "#14b8a6", "#3b82f6", "#8b5cf6", "#ec4899",
            "#06b6d4", "#84cc16", "#f43f5e", "#a855f7"
    };
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

    public void setOnNavigateBack(Runnable r) { this.onNavigateBack = r; }


    public void setListener(QuickEntryListener listener) {
        this.listener = listener;
    }


    public String getAmountText()        { return etAmountInput.getText(); }
    public String getSelectedCategory()  { return selectedCategory; }
    public String getNewCategoryText()   { return newCategoryField.getText(); }

    @Override
    public void printScreen() {
        exitEditMode();
        etAmountInput.clear();
        newCategoryField.clear();
        selectedCategory = null;
        currentlySelectedTile = null;

    }

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

    public void showCategoriesFromCategoryList(List<Category> categories) {
        List<String> names = new ArrayList<>();
        for (Category c : categories) names.add(c.getCategoryName());
        showCategories(names);
    }


    private VBox buildCategoryTile(String categoryName, String color) {
        VBox tile = new VBox(8);
        tile.setAlignment(Pos.CENTER);
        tile.setStyle(
                "-fx-background-color: " + color + "; -fx-padding: 12; " +
                        "-fx-background-radius: 12; -fx-cursor: hand;"
        );

        Label label = new Label(categoryName);
        label.setFont(Font.font("System", 14));
        label.setTextFill(Color.web("#38bdf8"));
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextFill(Color.WHITE);

        tile.getChildren().add(label);

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

        tile.setOnMouseClicked(e -> selectCategory(categoryName, tile, color));
        return tile;
    }

    private void selectCategory(String categoryName, VBox tile, String color) {
        if (currentlySelectedTile != null) {
            currentlySelectedTile.setStyle(
                    "-fx-background-color: rgba(14, 165, 233, 0.15);" +
                            "-fx-padding: 12; -fx-background-radius: 12; -fx-cursor: hand;" +
                            "-fx-border-color: rgba(14, 165, 233, 0.3); -fx-border-radius: 12;"
            );
            ((Label) currentlySelectedTile.getChildren().get(0))
                    .setTextFill(Color.web("#38bdf8"));
        }



        tile.setStyle(
                "-fx-background-color: " + color + "; -fx-padding: 12; " +
                        "-fx-background-radius: 12; -fx-cursor: hand; " +
                        "-fx-border-color: white; -fx-border-width: 3; -fx-border-radius: 12; -fx-opacity: 0.85;"
        );

        selectedCategory = categoryName;
        currentlySelectedTile = tile;
        currentlySelectedColor = color;
    }

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



    public void enterEditMode(Transaction transaction) {
        isEditMode = true;
        editingTransaction = transaction;
        etAmountInput.setText(String.valueOf(transaction.getTransactionAmount()));
        selectCategoryByName(transaction.getTransactionCategory().getCategoryName());
        btnSubmitExpense.setText("UPDATE EXPENSE");
    }

    public void exitEditMode() {
        isEditMode = false;
        editingTransaction = null;
        if (btnSubmitExpense != null) btnSubmitExpense.setText("CONFIRM EXPENSE");
    }


    public void clearNewCategory() {
        if (newCategoryField != null){
            newCategoryField.clear();
        }
    }

    public void showErrorMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public void showSavedConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Expense saved successfully!");
        alert.showAndWait();
    }




    public void showUpdateConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Transaction updated successfully!");
        alert.showAndWait();
    }


    @FXML
    public void onBackClicked() {
        if (listener != null && listener.onBackClicked()) {
            if (onNavigateBack != null) onNavigateBack.run();
        }
    }

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

    @FXML
    private void onAddCategoryClicked() {
        listener.onAddCategoryClicked(getNewCategoryText());
    }

}


