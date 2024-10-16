package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Account.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public int addAccount(Account account) throws SQLException {
        String query = "INSERT INTO accounts (name, group_name) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, account.getAccountName());
            ps.setString(2, account.getGroupName());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setId(rs.getInt(1));
                    return account.getId();
                } else {
                    throw new SQLException("Creating accounts failed, no ID Obtained");
                }
            }
        }
    }

    public void deleteAccount(String name) throws SQLException {
        String query = "DELETE FROM accounts WHERE name = ?";
        try (Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
    }

    public void updateAccount(String name, String groupName) throws SQLException {
        String query = "UPDATE accounts SET name = ?, group_name = ? WHERE name = ?";
        try (Connection conn = Database.getConnection();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);
            ps.setString(2, groupName);
            ps.setString(3, name);
            ps.executeUpdate();
        }
    }

    public List<Account> getAllAccounts() throws SQLException {
        String query = "SELECT * FROM accounts";
        List<Account> accoutns = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)
        ) {
            while (rs.next()) {
                Account accounts = new Account(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("group_name")
                );
                accoutns.add(accounts);
            }
        }
        return accoutns;
    }

    public double[] getTotalBalanceAndLiabilities() throws SQLException {
        String query = "SELECT account_id, SUM(amount) AS total_balance FROM transactions GROUP BY account_id";
        double totalAssessts = 0, totalLiabilities = 0;

        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                double balance = rs.getDouble("total_balance");
                if (balance > 0) {
                    totalAssessts += balance;
                } else {
                    totalLiabilities += balance;
                }
            }
        }

        return new double[]{totalAssessts, totalLiabilities};
    }
}
