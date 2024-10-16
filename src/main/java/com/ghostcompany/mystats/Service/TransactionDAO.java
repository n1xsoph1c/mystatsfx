package com.ghostcompany.mystats.Service;

import com.ghostcompany.mystats.Model.Account.ETransactionType;
import com.ghostcompany.mystats.Model.Account.Transaction;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public int addTransaction(Transaction transaction) throws SQLException {
        String query = "insert into transactions (account_id, amount, description, type) values(?,?,?,?)";
        try (
                Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
        ) {
                ps.setInt(1, transaction.getAccount_id());
                ps.setDouble(2, transaction.getAmount());
                ps.setString(3, transaction.getDescription());
                ps.setString(4, transaction.getType().toString().toUpperCase());
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        transaction.setTransaction_id(rs.getInt(1));
                        return transaction.getTransaction_id();
                    } else {
                        throw new SQLException("Failed to insert transaction on account " + transaction.getAccount_id());
                    }
                }
        }
    }

    public List<Transaction> getTransactions(int account_id) throws SQLException {
        String query = "select * from transactions where account_id=?";
        List<Transaction> transactions = new ArrayList<>();

        try(
                Connection conn = Database.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ) {
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

    public double getTotalBalanceByAccount(int account_id) throws SQLException {
        String query = "select SUM(amount) AS total_balance from transactions where account_id=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_balance");
            }
        }
        return 0;
    }

    public void updateTransaction(int transactionId, Transaction updatedTransaction) throws SQLException {
        String sql = "UPDATE transactions SET amount=?, description=?, type=? WHERE id=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setDouble(1, updatedTransaction.getAmount());
            ps.setString(2, updatedTransaction.getDescription());
            ps.setString(3, updatedTransaction.getType().toString().toUpperCase());
            ps.setInt(4, transactionId);
            ps.executeUpdate();
        }
    }

    public void deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id=?";
        try (Connection conn = Database.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
            ps.setInt(1, transactionId);
            ps.executeUpdate();
        }
    }
}
