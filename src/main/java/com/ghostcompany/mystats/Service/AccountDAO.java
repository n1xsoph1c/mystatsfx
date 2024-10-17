package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Account.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    private static final String INSERT_SQL = "INSERT INTO accounts (name, group_name) VALUES (?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM accounts";
    private static final String UPDATE_SQL = "UPDATE accounts SET name = ?, group_name = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM accounts WHERE id = ?";

    // Add a new account and return its ID.
    public int addAccount(Account account) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, account.getAccountName());
            ps.setString(2, account.getGroupName());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setId(rs.getInt(1));
                    return account.getId();
                }
            }
        }
        throw new SQLException("Failed to insert account.");
    }

    // Retrieve all accounts from the database.
    public List<Account> getAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("group_name")
                );
                accounts.add(account);
            }
        }
        return accounts;
    }

    // Update an account by ID.
    public boolean updateAccount(Account account) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, account.getAccountName());
            ps.setString(2, account.getGroupName());
            ps.setInt(3, account.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // Delete an account by ID.
    public boolean deleteAccount(int accountId) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, accountId);
            return ps.executeUpdate() > 0;
        }
    }
}
