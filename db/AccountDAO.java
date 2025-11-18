package main.com.banking.db;

import main.com.banking.model.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    public void saveAccount(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, customer_id, account_type, branch, balance, employer_name, employer_address) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getOwner().getCustomerId());
            stmt.setString(3, getAccountType(account));
            stmt.setString(4, account.getBranch());
            stmt.setBigDecimal(5, account.getBalance());

            if (account instanceof ChequeAccount) {
                ChequeAccount chequeAccount = (ChequeAccount) account;
                stmt.setString(6, chequeAccount.getEmployerName());
                stmt.setString(7, chequeAccount.getEmployerAddress());
            } else {
                stmt.setNull(6, Types.VARCHAR);
                stmt.setNull(7, Types.VARCHAR);
            }

            stmt.executeUpdate();
            System.out.println("Account saved: " + account.getAccountNumber());
        }
    }

    public Account findAccountByNumber(String accountNumber, CustomerDAO customerDAO) throws SQLException {
        String sql = "SELECT a.*, c.* FROM accounts a " +
                "JOIN customers c ON a.customer_id = c.customer_id " +
                "WHERE a.account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs, customerDAO);
                }
            }
        }
        return null;
    }

    public List<Account> findAccountsByCustomerId(String customerId, CustomerDAO customerDAO) throws SQLException {
        String sql = "SELECT a.*, c.* FROM accounts a " +
                "JOIN customers c ON a.customer_id = c.customer_id " +
                "WHERE a.customer_id = ? ORDER BY a.opened_date DESC";
        List<Account> accounts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs, customerDAO));
                }
            }
        }
        return accounts;
    }

    public List<Account> findAllAccounts(CustomerDAO customerDAO) throws SQLException {
        String sql = "SELECT a.*, c.* FROM accounts a " +
                "JOIN customers c ON a.customer_id = c.customer_id " +
                "ORDER BY a.opened_date DESC";
        List<Account> accounts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs, customerDAO));
            }
        }
        return accounts;
    }

    public void updateAccountBalance(String accountNumber, BigDecimal newBalance) throws SQLException {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, newBalance);
            stmt.setString(2, accountNumber);
            stmt.executeUpdate();
        }
    }

    private String getAccountType(Account account) {
        if (account instanceof SavingsAccount) {
            return "SAVINGS";
        } else if (account instanceof InvestmentAccount) {
            return "INVESTMENT";
        } else if (account instanceof ChequeAccount) {
            return "CHEQUE";
        } else {
            throw new IllegalArgumentException("Unknown account type: " + account.getClass().getSimpleName());
        }
    }

    private Account mapResultSetToAccount(ResultSet rs, CustomerDAO customerDAO) throws SQLException {
        String accountNumber = rs.getString("account_number");
        String accountType = rs.getString("account_type");
        String branch = rs.getString("branch");
        BigDecimal balance = rs.getBigDecimal("balance");

        String customerId = rs.getString("customer_id");
        Customer customer = customerDAO.findCustomerById(customerId);

        if (customer == null) {
            throw new SQLException("Customer not found for ID: " + customerId);
        }

        switch (accountType) {
            case "SAVINGS":
                return new SavingsAccount(accountNumber, customer, branch, balance);

            case "INVESTMENT":
                return new InvestmentAccount(accountNumber, customer, branch, balance);

            case "CHEQUE":
                String employerName = rs.getString("employer_name");
                String employerAddress = rs.getString("employer_address");
                return new ChequeAccount(accountNumber, customer, branch, balance, employerName, employerAddress);

            default:
                throw new IllegalArgumentException("Unknown account type in database: " + accountType);
        }
    }
}