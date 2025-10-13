package com.banking.model;

import java.math.BigDecimal;

public abstract class Account {
    private static int accountCounter = 1; 

    protected String accountNumber;
    protected BigDecimal balance;
    protected String branch;
    protected Customer owner;

    public Account(Customer owner, String branch, BigDecimal initialBalance) {
        this.accountNumber = String.valueOf(accountCounter++); 
        this.owner = owner;
        this.branch = branch;
        this.balance = initialBalance;
    }

    public void deposit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            balance = balance.add(amount);
            System.out.println("Deposited: BWP " + amount + " to account " + accountNumber);
        } else {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
    }

    public abstract void withdraw(BigDecimal amount);

    // Getters
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public String getBranch() { return branch; }
    public Customer getOwner() { return owner; }
}