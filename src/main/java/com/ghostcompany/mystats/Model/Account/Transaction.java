package com.ghostcompany.mystats.Model.Account;

import java.time.LocalDate;
import java.util.Date;

public class Transaction {
    private LocalDate date;
    private double amount;
    private String description;
    private ETransactionType transactionType;
    private int account_id;
    private int transaction_id;

    public Transaction(int transaction_id,int  account_id, double amount, String description, LocalDate date, ETransactionType transactionType) {
        this.transaction_id = transaction_id;
        this.account_id = account_id;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return transactionType == ETransactionType.WITHDRAWAL ? -amount : amount;
    }

    public String getDescription() { return description; }
    public int getAccount_id() { return account_id; }
    public int getTransaction_id() { return transaction_id; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setAccount_id(int account_id) { this.account_id = account_id; }
    public void setTransaction_id(int transaction_id) { this.transaction_id = transaction_id; }
    public void setTransactionType(ETransactionType transactionType) { this.transactionType = transactionType; }

    public LocalDate getDate() {
        return date;
    }

    public ETransactionType getType() {
        return transactionType;
    }


}
