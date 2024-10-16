package com.ghostcompany.mystats.Model.Account;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private int id;
    private String name;
    private String groupName;
    private List<Transaction> transactions;


    public Account(int id, String name, String groupName) {
        this.id = id;
        this.groupName = groupName;
        this.name = name;
        this.transactions = new ArrayList<>();
    }


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAccountName() {
        return name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setAccountName(String accountName) {
        this.name = accountName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public double getTotalAmount() {
        double totalAmount = 0;
        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmount();
        }

        return totalAmount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

}
