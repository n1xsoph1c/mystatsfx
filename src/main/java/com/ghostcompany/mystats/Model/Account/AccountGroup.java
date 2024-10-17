package com.ghostcompany.mystats.Model.Account;

import com.ghostcompany.mystats.Service.AccountDAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AccountGroup {
    private int id;
    private String name;
    private Map<String, Account> accounts = new HashMap<>();

    public AccountGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    public void addAccount(Account account) {
        accounts.put(account.getAccountName(), account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account.getAccountName());
    }

    public double getGroupTotal() throws SQLException {
        // Fetch all accounts and filter based on the group name
        List<Account> accounts = new AccountDAO().getAllAccounts()
                .stream()
                .filter(account -> account.getGroupName().equals(this.getName())) // Call getName() method
                .toList(); // Collecting the filtered accounts into a list

        double balance = 0;
        for (Account account : accounts) {
            balance += account.getTotalAmount(); // Summing up the total amount from the filtered accounts
        }
        return balance; // Returning the total balance
    }


    public Map<String, Account> getAccounts() {
        return accounts;
    }
}
