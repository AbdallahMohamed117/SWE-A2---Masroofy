package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.util.List;

public class QuickEntryView implements AbstractView {

    @FXML private TextField etAmountInput;
    @FXML private Button btnSubmitExpense;
    @FXML private GridPane categoryGrid;
    @FXML private TextField newCategoryField;

    private String selectedCategory = null;
    private VBox currentlySelectedTile = null;

    @Override
    public void printScreen() {
        etAmountInput.clear();
        newCategoryField.clear();
        selectedCategory = null;
        currentlySelectedTile = null;
        if (categoryGrid != null) categoryGrid.getChildren().clear();
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
        for (String cat : categories) {
            VBox tile = buildCategoryTile(cat);
            categoryGrid.add(tile, col, row);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    private VBox buildCategoryTile(String categoryName) {
        VBox tile = new VBox(8);
        tile.setAlignment(Pos.CENTER);
        tile.setStyle("-fx-background-color: #f3f4f6; -fx-padding: 12; -fx-background-radius: 12; -fx-cursor: hand;");

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
            currentlySelectedTile.setStyle("-fx-background-color: #f3f4f6; -fx-padding: 12; -fx-background-radius: 12; -fx-cursor: hand;");
            ((Label) currentlySelectedTile.getChildren().get(0)).setTextFill(Color.BLACK);
        }

        tile.setStyle("-fx-background-color: #3b82f6; -fx-padding: 12; -fx-background-radius: 12; -fx-cursor: hand;");
        ((Label) tile.getChildren().get(0)).setTextFill(Color.WHITE);

        selectedCategory = categoryName;
        currentlySelectedTile = tile;
    }

    @FXML private void onBackClicked() { }

    @FXML private void onSubmitExpense() {
        onExpenseSubmitted(getAmountText(), getSelectedCategory());
    }

    @FXML private void onAddCategoryClicked() {
        onCategoryAdded(getNewCategoryText());
    }

    public void onExpenseSubmitted(String amountText, String category) { }
    public void onCategoryAdded(String categoryName) { }
}