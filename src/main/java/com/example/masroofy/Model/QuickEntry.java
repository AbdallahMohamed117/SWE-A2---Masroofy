package com.example.masroofy.Model;

import com.example.masroofy.Database.DatabaseConnection;
import com.example.masroofy.Model.Entity.Category;
import com.example.masroofy.Model.Entity.Transaction;



public class QuickEntry extends AbstractModel {
    protected QuickEntry() {
        super();
    }

    public void addTransaction(Transaction transaction) {}

    public void addCategory(Category category) {}

    public void removeCategory(Category category) {}
}