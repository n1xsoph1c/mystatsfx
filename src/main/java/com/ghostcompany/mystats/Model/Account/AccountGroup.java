package com.ghostcompany.mystats.Model.Account;

public class AccountGroup {
    private int id;
    private String name;
    private String description;

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
