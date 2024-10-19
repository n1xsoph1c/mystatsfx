package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Account.AccountGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountGroupDAO {

    // SQL Queries
    private static final String INSERT_SQL = "INSERT INTO account_groups (name) VALUES (?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM account_groups";
    private static final String UPDATE_SQL = "UPDATE account_groups SET name = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM account_groups WHERE id = ?";

    // Add a new account group and return the generated ID.
    public int addAccountGroup(AccountGroup group) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, group.getName());
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating account group failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    group.setId(rs.getInt(1));
                    return group.getId();
                } else {
                    throw new SQLException("Creating account group failed, no ID obtained.");
                }
            }
        }
    }

    // Retrieve all account groups from the database.
    public ObservableList<AccountGroup> getAllAccountGroups() throws SQLException {
        ObservableList<AccountGroup> groups = FXCollections.observableArrayList();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AccountGroup group = new AccountGroup(
                        rs.getInt("id"),
                        rs.getString("name")
                );
                groups.add(group);
            }
        }
        return groups;
    }

    // Update an existing account group.
    public boolean updateAccountGroup(int groupId, String newName) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, newName);
            ps.setInt(2, groupId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Delete an account group by ID.
    public boolean deleteAccountGroup(int groupId) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, groupId);

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }
}
