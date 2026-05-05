package com.example.masroofy.util;

public class QuickEntryValidator {
    public boolean validate(String amountText, String category) {
        if (amountText == null || amountText.trim().isEmpty()) {
            return false;
        }

        try {
            double amount = Double.parseDouble(amountText);
            if (amount <= 0) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        if (category == null || category.trim().isEmpty()) {
            return false;
        }

        return true;
    }
}
