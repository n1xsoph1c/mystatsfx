package com.ghostcompany.mystats.Model.Account;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private int id;
    private String name;
    private String description;
    private AccountGroup accountGroup;
    private List<Transaction> transactions = new ArrayList<>();
    private double balance;

    public Account(int id, String name, String description, AccountGroup accountGroup) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.accountGroup = accountGroup;
    }

    public Account() {}

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
        this.balance = 0;
        for (Transaction t : transactions) {
            if (t.getTransactionType() == ETransactionType.DEPOSIT) {
                this.balance += t.getAmount();
            } else if (t.getTransactionType() == ETransactionType.WITHDRAWAL) {
                this.balance -= t.getAmount();
            }
        }
        return this.balance;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nAccount { ");
        sb.append("id: \n").append(id);
        sb.append(", name: \n").append(name);
        sb.append(", description: \n").append(description);
        sb.append(", accountGroup: \n").append(accountGroup.getName());
        sb.append(", transactions: [\n");

        for (int i = 0; i < transactions.size(); i++) {
            sb.append(transactions.get(i).toString());
            if (i < transactions.size() - 1) sb.append(", \n");
        }

        sb.append("],\n balance: \n").append(getTotalAmount());
        sb.append(" }");
        return sb.toString();
    }

}
