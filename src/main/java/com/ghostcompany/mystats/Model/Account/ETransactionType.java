package com.ghostcompany.mystats.Model.Account;

public enum ETransactionType {
    WITHDRAWAL,
    DEPOSIT;

    @Override
    public String toString() {
        return name(); // or return a more descriptive string if needed
    }
}
