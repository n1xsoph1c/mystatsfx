package com.ghostcompany.mystats.Model.Account;

import java.time.LocalDate;
import java.util.Date;

public class Transaction {
    private int id;
    private Date date;
    private double amount;
    private String description;
    private ETransactionType transactionType;

    public Transaction(int id, Date date, double amount, String description, ETransactionType transactionType) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.description = description;
        this.transactionType = transactionType;
    }

    public int getId() { return id; }
    public Date getDate() { return date; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
    public ETransactionType getTransactionType() { return transactionType; }
    public void setId(int id) { this.id = id; }
    public void setDate(Date date) { this.date = date; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDescription(String description) { this.description = description; }
    public void setTransactionType(ETransactionType transactionType) { this.transactionType = transactionType; }


    @Override
    public String toString() {
        return "Transaction { " +
                "id: " + id +
                ", date: " + date +
                ", amount: " + amount +
                ", description: '" + description + '\'' +
                ", transactionType: " + transactionType +
                " }";
    }

}
