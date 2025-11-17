package main.com.banking.model;

import java.math.BigDecimal;

public abstract class Account {
    protected String accountNumber;
    protected Customer owner;
    protected String branch;
    protected BigDecimal balance;

    public Account(Customer owner, String branch, BigDecimal balance) {
        this.accountNumber = generateAccountNumber();
        this.owner = owner;
        this.branch = branch;
        this.balance = balance;
    }

    // Constructor for database loading
    public Account(String accountNumber, Customer owner, String branch, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.branch = branch;
        this.balance = balance;
    }

    protected String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public Customer getOwner() { return owner; }
    public String getBranch() { return branch; }
    public BigDecimal getBalance() { return balance; }

    // Business methods
    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
        if (amount.compareTo(balance) > 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.balance = this.balance.subtract(amount);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", branch='" + branch + '\'' +
                '}';
    }
}