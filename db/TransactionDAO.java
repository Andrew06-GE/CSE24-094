package main.com.banking.db;

import main.com.banking.model.Transaction;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    public void saveTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (transaction_id, account_number, transaction_type, amount, balance_after, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getTransactionId());
            stmt.setString(2, transaction.getAccountNumber());
            stmt.setString(3, transaction.getType());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setBigDecimal(5, transaction.getBalanceAfter());
            stmt.setString(6, transaction.toString());

            stmt.executeUpdate();
        }
    }

    public List<Transaction> findTransactionsByAccount(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactions;
    }

    public List<Transaction> findAllTransactions() throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY transaction_date DESC";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(mapResultSetToTransaction(rs));
            }
        }
        return transactions;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        String transactionId = rs.getString("transaction_id");
        String accountNumber = rs.getString("account_number");
        String type = rs.getString("transaction_type");
        BigDecimal amount = rs.getBigDecimal("amount");
        BigDecimal balanceAfter = rs.getBigDecimal("balance_after");
        Timestamp timestamp = rs.getTimestamp("transaction_date");

        // Create Transaction object - the ID is generated in constructor
        Transaction transaction = new Transaction(accountNumber, type, amount, balanceAfter);

        return transaction;
    }
}