package com.ghostcompany.mystats.Model.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountGroup {
    private int id;
    private String name;
    private Map<String, Account> accounts;

    public AccountGroup(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.accounts = new HashMap<>();
    }

    public void addAccount(Account account) { accounts.put(account.getAccountName(), account); }

    public double getGroupTotal() {
        return accounts.values().stream().mapToDouble(Account::getTotalAmount).sum();
    }

    public void removeAccount(Account account) {
        accounts.remove(account.getAccountName());
    }

    public void createAccountGroup() {
        // Create Account Group on sql
    }

    public void deleteAccountGroup() {
        // Delete Account Group on sql
    }

    public void updateAccountGroup() {
        // Update Account Group on sql
    }


}
