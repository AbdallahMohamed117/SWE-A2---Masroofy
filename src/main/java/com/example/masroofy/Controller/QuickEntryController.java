package com.example.masroofy.Controller;

import com.example.masroofy.Model.Entity.*;
import com.example.masroofy.Model.*;
import com.example.masroofy.View.*;

public class QuickEntryController implements AbstractController {
    @Override
    public void PrintView(AbstractView view) {}

    public boolean validateAddTransaction(int transactionAmount, Category category) {
        return true;
    }

    public boolean validateAddCategory(String categoryName) {
        return true;
    }
}