package com.ghostcompany.mystats.Model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountGroup {
    private int id;
    private String name;
    private String description;
    private List<Account> accounts = new ArrayList<Account>();

    public AccountGroup(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }

    public List<Account> getAccounts() { return accounts; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AccountGroup { ");
        sb.append("id: ").append(id);
        sb.append(", name: ").append(name);
        sb.append(", description: ").append(description);
        sb.append(", accounts: [");

        for (int i = 0; i < accounts.size(); i++) {
            sb.append(accounts.get(i).toString());
            if (i < accounts.size() - 1) sb.append(", ");
        }

        sb.append("] }");
        return sb.toString();
    }

}
