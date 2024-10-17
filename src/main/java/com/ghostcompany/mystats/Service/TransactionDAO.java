package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Account.ETransactionType;
import com.ghostcompany.mystats.Model.Account.Transaction;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    // SQL Queries
    private static final String INSERT_SQL = "INSERT INTO transactions (account_id, amount, description, type, date) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_SQL = "SELECT * FROM transactions WHERE account_id = ?";
    private static final String SUM_SQL = "SELECT SUM(amount) AS total_balance FROM transactions WHERE account_id = ?";
    private static final String UPDATE_SQL = "UPDATE transactions SET amount = ?, description = ?, type = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM transactions WHERE id = ?";

    // Add a new transaction and return its ID.
    public int addTransaction(Transaction transaction) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, transaction.getAccountId());
            ps.setDouble(2, transaction.getAmount());
            ps.setString(3, transaction.getDescription());
            ps.setString(4, transaction.getType().toString().toUpperCase());
            ps.setTimestamp(5, Timestamp.valueOf(transaction.getDate().atTime(LocalTime.now())));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    transaction.setTransactionId(rs.getInt(1));
                    return transaction.getTransactionId();
                } else {
                    throw new SQLException("Failed to insert transaction on account " + transaction.getAccountId());
                }
            }
        }
    }

    // Retrieve all transactions for a specific account.
    public List<Transaction> getTransactions(int account_id) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_SQL)) {

            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("id"),
                        rs.getInt("account_id"),
                        rs.getDouble("amount"),
                        rs.getString("description"),
                        rs.getTimestamp("date").toLocalDateTime().toLocalDate(),
                        ETransactionType.valueOf(rs.getString("type"))
                );
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    // Get total balance for a specific account.
    public double getTotalBalanceByAccount(int account_id) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(SUM_SQL)) {

            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_balance");
            }
        }
        return 0;
    }

    // Update an existing transaction.
    public void updateTransaction(int transactionId, Transaction updatedTransaction) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setDouble(1, updatedTransaction.getAmount());
            ps.setString(2, updatedTransaction.getDescription());
            ps.setString(3, updatedTransaction.getType().toString().toUpperCase());
            ps.setInt(4, transactionId);
            ps.executeUpdate();
        }
    }

    // Delete a transaction by ID.
    public void deleteTransaction(int transactionId) throws SQLException {
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, transactionId);
            ps.executeUpdate();
        }
    }
}
