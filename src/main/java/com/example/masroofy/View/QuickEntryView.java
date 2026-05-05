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

    private String selectedCategory = null;
    private VBox currentlySelectedTile = null;
    private QuickEntryListener listener;
    private Runnable onNavigateBack;

    private boolean isEditMode = false;
    private Transaction editingTransaction;

    public void setOnNavigateBack(Runnable r) { this.onNavigateBack = r; }

    @Override
    public void printScreen() {
        exitEditMode();
        etAmountInput.clear();
        newCategoryField.clear();
        selectedCategory = null;
        currentlySelectedTile = null;
        if (categoryGrid != null) categoryGrid.getChildren().clear();
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

    private void selectCategoryByName(String categoryName) {
        if (categoryGrid == null) return;
        for (javafx.scene.Node node : categoryGrid.getChildren()) {
            if (node instanceof VBox) {
                VBox tile = (VBox) node;
                if (!tile.getChildren().isEmpty() && tile.getChildren().get(0) instanceof Label) {
                    Label label = (Label) tile.getChildren().get(0);
                    if (categoryName.equals(label.getText())) {
                        selectCategory(categoryName, tile);
                        return;
                    }
                }
            }
        }
    }

    public void setListener(QuickEntryListener listener) {
        this.listener = listener;
    }

    public String getAmountText()        { return etAmountInput.getText(); }
    public String getSelectedCategory()  { return selectedCategory; }
    public String getNewCategoryText()   { return newCategoryField.getText(); }
    public void   clearNewCategory()     { if (newCategoryField != null) newCategoryField.clear(); }

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

    public void showCategories(List<String> categories) {
        if (categoryGrid == null) return;
        categoryGrid.getChildren().clear();
        int col = 0, row = 0;
        for (String catName : categories) {
            VBox tile = buildCategoryTile(catName);
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

    private VBox buildCategoryTile(String categoryName) {
        VBox tile = new VBox(8);
        tile.setAlignment(Pos.CENTER);
        tile.setStyle(
                "-fx-background-color: #f3f4f6; -fx-padding: 12; " +
                        "-fx-background-radius: 12; -fx-cursor: hand;"
        );

        Label label = new Label(categoryName);
        label.setFont(Font.font("System", 14));
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);

        tile.getChildren().add(label);

        tile.setOnMouseClicked(e -> selectCategory(categoryName, tile));
        return tile;
    }

    private void selectCategory(String categoryName, VBox tile) {
        if (currentlySelectedTile != null) {
            currentlySelectedTile.setStyle(
                    "-fx-background-color: #f3f4f6; -fx-padding: 12; " +
                            "-fx-background-radius: 12; -fx-cursor: hand;"
            );
            ((Label) currentlySelectedTile.getChildren().get(0)).setTextFill(Color.BLACK);
        }

        tile.setStyle(
                "-fx-background-color: #3b82f6; -fx-padding: 12; " +
                        "-fx-background-radius: 12; -fx-cursor: hand;"
        );
        ((Label) tile.getChildren().get(0)).setTextFill(Color.WHITE);

        selectedCategory = categoryName;
        currentlySelectedTile = tile;
    }

    @FXML
    public void onBackClicked() {
        if (listener != null && listener.onBackClicked()) {
            if (onNavigateBack != null) onNavigateBack.run();
        }
    }

    @FXML private void onSubmitExpense() {
        if (isEditMode && editingTransaction != null) {
            editingTransaction.setTransactionAmount(Double.parseDouble(getAmountText()));
            Category c = new Category();
            c.setCategoryName(getSelectedCategory());
            editingTransaction.setTransactionCategory(c);
            listener.onEditSubmitted(editingTransaction);
        } else {
            String amountText = getAmountText();

            if (amountText == null || amountText.isEmpty()) {
                showErrorMessage("Please enter an amount.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException e) {
                showErrorMessage("Please enter a valid number.");
                return;
            }

            if (amount <= 0) {
                showErrorMessage("Amount must be greater than zero.");
                return;
            }

            if (selectedCategory == null) {
                showErrorMessage("Please select a category.");
                return;
            }

            Category c = new Category();
            c.setCategoryName(selectedCategory);
            Transaction t = new Transaction();
            t.setTransactionAmount(amount);
            t.setTransactionCategory(c);
            t.setTransactionTimestamp(System.currentTimeMillis());

            listener.onSubmitExpense(t);
            showSavedConfirmation();
        }
    }

    @FXML
    private void onAddCategoryClicked() {
        listener.onAddCategoryClicked(getNewCategoryText());
    }

    public void showUpdateConfirmation() {
        System.out.println("Test");
    }
}
