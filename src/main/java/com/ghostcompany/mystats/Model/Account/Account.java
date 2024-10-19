package com.ghostcompany.mystats.Model.Account;

import com.ghostcompany.mystats.Service.AccountDAO;
import com.ghostcompany.mystats.Service.TransactionDAO;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private int id;
    private String name;
    private String groupName;
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private List<Transaction> transactions = new ArrayList<>();
    private final AccountDAO accountDAO = new AccountDAO();

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

    public ObservableList<Transaction> getTransactions() throws SQLException {
        return transactionDAO.getTransactions(this.id);
    }

    public ObservableList<Account> getAccounts() throws SQLException {
        return accountDAO.getAllAccounts();
    }

    public double getTotalBalance() throws SQLException {
        return transactionDAO.getTotalBalanceByAccount(getId());
    }

    public void addAccount(Account account) throws SQLException {
        accountDAO.addAccount(account);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public double getTotalAmount() throws SQLException {
        return this.getTransactions().stream().mapToDouble(Transaction::getBalance).sum();
    }
}
