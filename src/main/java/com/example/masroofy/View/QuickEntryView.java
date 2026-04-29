package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.List;

public class QuickEntryView implements AbstractView {

    @FXML private TextField etAmountInput;
    @FXML private Button btnSubmitExpense;
    @FXML private GridPane categoryGrid;
    @FXML private TextField newCategoryField;

    private String selectedCategory = null;

    @Override
    public void printScreen() {}



    public void showCategories(List<String> categories) {
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

    public void onExpenseSubmitted(double amount, String category) {
        // TODO: QuickEntryController يتعامل مع الـ logic
    }


    @FXML
    private void onBackClicked() {
    }

    @FXML
    private void onSubmitExpense() {
        String amountText = etAmountInput.getText();

        if (amountText == null || amountText.isEmpty()) {
            showErrorMessage("Please enter a valid number.");
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

        onExpenseSubmitted(amount, selectedCategory);
    }

    @FXML
    private void onAddCategoryClicked() {
        if (newCategoryField == null) return;
        String newCategory = newCategoryField.getText();
        if (newCategory == null || newCategory.isEmpty()) {
            showErrorMessage("Please enter a category name.");
            return;
        }
        newCategoryField.clear();
    }

    @FXML
    private void onFoodClicked() {
        selectedCategory = "Food";
    }

    @FXML
    private void onTransportClicked() {
        selectedCategory = "Transport";
    }

    @FXML
    private void onGiftsClicked() {
        selectedCategory = "Gifts";
    }
}