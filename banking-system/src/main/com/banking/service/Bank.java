package main.com.banking.service;

import main.com.banking.db.*;
import main.com.banking.model.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Bank {
    private CustomerDAO customerDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    public Bank() {
        this.customerDAO = new CustomerDAO();
        this.accountDAO = new AccountDAO();
        this.transactionDAO = new TransactionDAO();
        System.out.println("Bank service initialized with database connectivity");
    }

    public void registerIndividualCustomer(String firstName, String surname, String address, LocalDate dateOfBirth) {
        try {
            Customer customer = new Customer(firstName, surname, address, dateOfBirth);
            customerDAO.saveCustomer(customer);
            System.out.println("REGISTERED INDIVIDUAL CUSTOMER: " + customer.getFullName() +
                    " (ID: " + customer.getCustomerId() + ")");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to register customer: " + e.getMessage(), e);
        }
    }

    public void registerCorporateCustomer(String companyName, String address) {
        try {
            Customer customer = new Customer(companyName, address);
            customerDAO.saveCustomer(customer);
            System.out.println("REGISTERED CORPORATE CUSTOMER: " + customer.getDisplayName() +
                    " (ID: " + customer.getCustomerId() + ")");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to register corporate customer: " + e.getMessage(), e);
        }
    }

    public Customer getCustomer(String customerId) {
        try {
            return customerDAO.findCustomerById(customerId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get customer: " + e.getMessage(), e);
        }
    }

    public Account openSavingsAccount(String customerId, String branch, BigDecimal initialBalance) {
        try {
            Customer customer = customerDAO.findCustomerById(customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found: " + customerId);
            }

            SavingsAccount account = new SavingsAccount(customer, branch, initialBalance);
            accountDAO.saveAccount(account);

            Transaction transaction = new Transaction(account.getAccountNumber(), "OPENING_DEPOSIT", initialBalance, initialBalance);
            transactionDAO.saveTransaction(transaction);

            System.out.println("OPENED SAVINGS ACCOUNT: " + account.getAccountNumber() +
                    " for " + customer.getDisplayName() +
                    " | Branch: " + branch +
                    " | Initial Balance: BWP " + initialBalance);

            return account;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open savings account: " + e.getMessage(), e);
        }
    }

    public Account openInvestmentAccount(String customerId, String branch, BigDecimal initialBalance) {
        try {
            Customer customer = customerDAO.findCustomerById(customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found: " + customerId);
            }

            InvestmentAccount account = new InvestmentAccount(customer, branch, initialBalance);
            accountDAO.saveAccount(account);

            Transaction transaction = new Transaction(account.getAccountNumber(), "OPENING_DEPOSIT", initialBalance, initialBalance);
            transactionDAO.saveTransaction(transaction);

            System.out.println("OPENED INVESTMENT ACCOUNT: " + account.getAccountNumber() +
                    " for " + customer.getDisplayName() +
                    " | Branch: " + branch +
                    " | Initial Balance: BWP " + initialBalance);

            return account;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open investment account: " + e.getMessage(), e);
        }
    }

    public Account openChequeAccount(String customerId, String branch, BigDecimal initialBalance, String employerName, String employerAddress) {
        try {
            Customer customer = customerDAO.findCustomerById(customerId);
            if (customer == null) {
                throw new IllegalArgumentException("Customer not found: " + customerId);
            }

            ChequeAccount account = new ChequeAccount(customer, branch, initialBalance, employerName, employerAddress);
            accountDAO.saveAccount(account);

            Transaction transaction = new Transaction(account.getAccountNumber(), "OPENING_DEPOSIT", initialBalance, initialBalance);
            transactionDAO.saveTransaction(transaction);

            System.out.println("OPENED CHEQUE ACCOUNT: " + account.getAccountNumber() +
                    " for " + customer.getDisplayName() +
                    " | Branch: " + branch +
                    " | Initial Balance: BWP " + initialBalance +
                    " | Employer: " + employerName);

            return account;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open cheque account: " + e.getMessage(), e);
        }
    }

    public void deposit(String accountNumber, BigDecimal amount) {
        try {
            Account account = accountDAO.findAccountByNumber(accountNumber, customerDAO);
            if (account == null) {
                throw new IllegalArgumentException("Account not found: " + accountNumber);
            }

            BigDecimal oldBalance = account.getBalance();
            account.deposit(amount);
            accountDAO.updateAccountBalance(accountNumber, account.getBalance());

            Transaction transaction = new Transaction(accountNumber, "DEPOSIT", amount, account.getBalance());
            transactionDAO.saveTransaction(transaction);

            // PRINT TO CONSOLE
            System.out.println("=== DEPOSIT SUCCESSFUL ===");
            System.out.println("Account: " + accountNumber);
            System.out.println("Amount: BWP " + amount);
            System.out.println("Previous Balance: BWP " + oldBalance);
            System.out.println("New Balance: BWP " + account.getBalance());
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("==========================");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to process deposit: " + e.getMessage(), e);
        }
    }

    public void withdraw(String accountNumber, BigDecimal amount) {
        try {
            Account account = accountDAO.findAccountByNumber(accountNumber, customerDAO);
            if (account == null) {
                throw new IllegalArgumentException("Account not found: " + accountNumber);
            }

            BigDecimal oldBalance = account.getBalance();
            account.withdraw(amount);
            accountDAO.updateAccountBalance(accountNumber, account.getBalance());

            Transaction transaction = new Transaction(accountNumber, "WITHDRAWAL", amount, account.getBalance());
            transactionDAO.saveTransaction(transaction);

            // PRINT TO CONSOLE
            System.out.println("=== WITHDRAWAL SUCCESSFUL ===");
            System.out.println("Account: " + accountNumber);
            System.out.println("Amount: BWP " + amount);
            System.out.println("Previous Balance: BWP " + oldBalance);
            System.out.println("New Balance: BWP " + account.getBalance());
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("============================");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to process withdrawal: " + e.getMessage(), e);
        }
    }

    public void applyMonthlyInterestToAll() {
        try {
            List<Account> accounts = accountDAO.findAllAccounts(customerDAO);
            int interestAppliedCount = 0;

            System.out.println("APPLYING MONTHLY INTEREST TO ALL ACCOUNTS...");

            for (Account account : accounts) {
                if (account instanceof SavingsAccount) {
                    SavingsAccount savingsAccount = (SavingsAccount) account;
                    BigDecimal oldBalance = savingsAccount.getBalance();

                    // Apply interest (assuming 2% annual interest = 0.1667% monthly)
                    BigDecimal monthlyInterestRate = new BigDecimal("0.001667");
                    BigDecimal interest = oldBalance.multiply(monthlyInterestRate);

                    savingsAccount.deposit(interest);
                    accountDAO.updateAccountBalance(savingsAccount.getAccountNumber(), savingsAccount.getBalance());

                    // Record interest transaction
                    Transaction transaction = new Transaction(
                            savingsAccount.getAccountNumber(),
                            "INTEREST",
                            interest,
                            savingsAccount.getBalance()
                    );
                    transactionDAO.saveTransaction(transaction);
                    interestAppliedCount++;

                    // PRINT TO CONSOLE
                    System.out.println("=== INTEREST APPLIED ===");
                    System.out.println("Account: " + savingsAccount.getAccountNumber());
                    System.out.println("Interest Amount: BWP " + interest);
                    System.out.println("Previous Balance: BWP " + oldBalance);
                    System.out.println("New Balance: BWP " + savingsAccount.getBalance());
                    System.out.println("========================");
                }
            }

            System.out.println("Monthly interest applied to " + interestAppliedCount + " savings accounts");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to apply monthly interest: " + e.getMessage(), e);
        }
    }

    public String viewAllCustomers() {
        try {
            List<Customer> customers = customerDAO.findAllCustomers();
            return customers.stream()
                    .map(c -> String.format("ID: %s | Name: %s | Type: %s | Address: %s",
                            c.getCustomerId(), c.getDisplayName(),
                            c.isCorporate() ? "Corporate" : "Individual", c.getAddress()))
                    .collect(Collectors.joining("\n"));
        } catch (SQLException e) {
            return "Error retrieving customers: " + e.getMessage();
        }
    }

    public String viewCustomerAccounts(String customerId) {
        try {
            List<Account> accounts = accountDAO.findAccountsByCustomerId(customerId, customerDAO);
            if (accounts.isEmpty()) {
                return "No accounts found for customer: " + customerId;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Accounts for Customer ID: ").append(customerId).append("\n");
            for (Account account : accounts) {
                sb.append(String.format("  Account: %s | Type: %s | Balance: BWP %.2f | Branch: %s\n",
                        account.getAccountNumber(),
                        account.getClass().getSimpleName(),
                        account.getBalance(),
                        account.getBranch()));
            }
            return sb.toString();
        } catch (SQLException e) {
            return "Error retrieving customer accounts: " + e.getMessage();
        }
    }

    public List<Transaction> getAllTransactions() {
        try {
            return transactionDAO.findAllTransactions();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get transactions: " + e.getMessage(), e);
        }
    }

    public String getBankSummary() {
        try {
            List<Customer> customers = customerDAO.findAllCustomers();
            List<Account> accounts = accountDAO.findAllAccounts(customerDAO);

            long totalCustomers = customers.size();
            long totalAccounts = accounts.size();
            BigDecimal totalBalance = accounts.stream()
                    .map(Account::getBalance)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return String.format(
                    "BANK SUMMARY REPORT\n" +
                            "===================\n" +
                            "Total Customers: %d\n" +
                            "Total Accounts: %d\n" +
                            "Total Balance: BWP %.2f\n" +
                            "===================",
                    totalCustomers, totalAccounts, totalBalance);

        } catch (SQLException e) {
            return "Error generating bank summary: " + e.getMessage();
        }
    }


    public String generateCustomerReport() {
        return viewAllCustomers();
    }

    public String generateAccountsSummary() {
        return getBankSummary();
    }

    public String generateTransactionReport() {
        try {
            List<Transaction> transactions = transactionDAO.findAllTransactions();
            if (transactions.isEmpty()) {
                return "No transactions found in the system.";
            }

            StringBuilder report = new StringBuilder();
            report.append("TRANSACTION REPORT\n");
            report.append("==================\n");
            report.append("Total Transactions: ").append(transactions.size()).append("\n\n");

            // Show last 10 transactions
            List<Transaction> recentTransactions = transactions.stream()
                    .limit(10)
                    .collect(Collectors.toList());

            for (Transaction transaction : recentTransactions) {
                report.append(transaction.toString()).append("\n");
            }

            if (transactions.size() > 10) {
                report.append("\n... and ").append(transactions.size() - 10).append(" more transactions");
            }

            return report.toString();

        } catch (SQLException e) {
            return "Error generating transaction report: " + e.getMessage();
        }
    }
}