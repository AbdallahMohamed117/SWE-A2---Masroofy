package com.example.masroofy.View;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.Date;
import java.util.List;
import com.example.masroofy.Model.Entity.Transaction;
import com.example.masroofy.Listener.HistoryListener;
public class HistoryView implements AbstractView {

    @FXML private Label totalExpensesLabel;
    @FXML private Label transactionCountLabel;
    @FXML private VBox transactionListVBox;
    @FXML private Button categoryFilterButton;
    @FXML private Button dateFilterButton;

    private HistoryListener listener;

    @Override
    public void printScreen() {}

    public void setListener(HistoryListener l) {
        listener = l;
    }
    public void showTransactions(List<Transaction> list) {
        transactionListVBox.getChildren().clear();

        if (list == null || list.isEmpty()) {
            showEmptyState();
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

    public void showEmptyState() {
        Label emptyLabel = new Label("No Transactions Found");
        emptyLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 14;");
        transactionListVBox.getChildren().add(emptyLabel);
    }

    public void showNoFilterResults() {
        transactionListVBox.getChildren().clear();
        Label noResults = new Label("No transactions found for the selected filter.");
        noResults.setStyle("-fx-text-fill: #64748b; -fx-font-size: 13;");
        transactionListVBox.getChildren().add(noResults);
    }

    public void showDeleteConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Transaction");
        alert.setHeaderText(null);
        alert.setContentText(
                "Are you sure you want to delete this? This will update your daily limit."
        );
        alert.showAndWait();
    }

    public void showUpdateConfirmation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Transaction Updated");
        alert.showAndWait();
    }

    public void showEditForm(Transaction transaction) {}

    @FXML
    private void onCategoryFilterClicked() {}

    @FXML
    private void onDateFilterClicked() {}

    @FXML
    private void onApplyFilterClicked() {
        applyFilter(null, null, null);
    }

    @FXML
    private void onAddTransactionClicked() {}

    @FXML
    private void onBackClicked() {
    }

    public void applyFilter(String category, Date from, Date to) {}

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
        Label amountLabel = new Label("- EGP " + t.getTransactionAmount());
        amountLabel.setStyle("-fx-text-fill: #fb7185; -fx-font-weight: bold;");
        Button editBtn = new Button("Edit");
        editBtn.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #2dd4bf;" +
                        "-fx-font-size: 10; -fx-padding: 0; -fx-cursor: hand; -fx-underline: true;"
        );
        editBtn.setOnAction(e -> showEditForm(t));
        amountBox.getChildren().addAll(amountLabel, editBtn);

        row.getChildren().addAll(icon, info, amountBox);
        return row;
    }

    private String getCategoryEmoji(String category) {
        if (category == null) return "💰";
        switch (category.toLowerCase()) {
            case "food": return "🍔";
            case "transport": return "🚗";
            case "entertainment": return "🎮";
            default: return "💰";
        }
    }
}