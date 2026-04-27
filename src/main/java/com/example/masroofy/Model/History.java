package com.example.masroofy.Model;

import com.example.masroofy.Database.DatabaseConnection;
import com.example.masroofy.Model.Entity.Transaction;
import java.util.List;

public class History extends AbstractModel {
    protected History() {
        super();
    }

    public List<Transaction> getTransactions() {
        return null;
    }

    public boolean editTransaction(Transaction transaction) {
        return false;
    }

    public boolean removeTransaction(Transaction transaction) {
        return false;
    }
}