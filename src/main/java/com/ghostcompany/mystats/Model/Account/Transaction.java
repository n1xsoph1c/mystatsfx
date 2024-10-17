package com.ghostcompany.mystats.Model.Account;

import java.time.LocalDate;

public class Transaction {
    private int transactionId;
    private int accountId;
    private double amount;
    private String description;
    private LocalDate date;
    private ETransactionType transactionType;

    public Transaction(int transactionId, int accountId, double amount,
                       String description, LocalDate date, ETransactionType transactionType) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.transactionType = transactionType;
    }

    public int getTransactionId() { return transactionId; }
    public int getAccountId() { return accountId; }
    public double getAmount() {
        return amount;
    }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public ETransactionType getType() { return transactionType; }

    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setTransactionType(ETransactionType type) { this.transactionType = type; }

    public double getBalance() { return this.transactionType == ETransactionType.DEPOSIT ? this.amount : this.amount * -1; }
}
