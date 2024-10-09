package com.ghostcompany.mystats.Model.Account;

import java.util.List;

public class Account {
    private int id;
    private String name;
    private String description;
    private AccountGroup accountGroup;
    private List<Transaction> transactions;
    private double balance;

    public Account(int id, String name, String description, AccountGroup accountGroup) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.accountGroup = accountGroup;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public AccountGroup getAccountGroup() { return accountGroup; }
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setAccountGroup(AccountGroup accountGroup) { this.accountGroup = accountGroup; }

    public List<Transaction> getTransactions() { return transactions; }
    public void setTransactions(List<Transaction> transactions) { this.transactions = transactions; }

    public void addTransaction(Transaction transaction) { this.transactions.add(transaction); }
    public void removeTransaction(Transaction transaction) { this.transactions.remove(transaction); }

    public double getTotalAmount() {
        this.transactions.forEach( t -> {
            if (t.getTransactionType() == ETransactionType.DEPOSIT) this.balance += t.getAmount();
            this.balance -= t.getAmount();
        });

        return this.balance;
    }
}
