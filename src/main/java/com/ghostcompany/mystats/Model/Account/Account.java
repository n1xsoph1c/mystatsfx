package com.ghostcompany.mystats.Model.Account;

import com.ghostcompany.mystats.Service.TransactionDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private int id;
    private String name;
    private String groupName;
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private List<Transaction> transactions = new ArrayList<>();

    public Account(int id, String name, String groupName) {
        this.id = id;
        this.name = name;
        this.groupName = groupName;
    }

    public Account() {}

    public int getId() { return id; }
    public String getAccountName() { return name; }
    public String getGroupName() { return groupName; }

    public void setId(int id) { this.id = id; }
    public void setAccountName(String name) { this.name = name; }
    public void setGroupName(String groupName) { this.groupName = groupName; }

    public List<Transaction> getTransactions() throws SQLException {
        return transactionDAO.getTransactions(this.id);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public double getTotalAmount() throws SQLException {
        return this.getTransactions().stream().mapToDouble(Transaction::getBalance).sum();
    }
}
