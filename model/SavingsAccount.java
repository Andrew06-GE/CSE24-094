package main.com.banking.model;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
    private BigDecimal interestRate;

    public SavingsAccount(Customer owner, String branch, BigDecimal balance) {
        super(owner, branch, balance);
        this.interestRate = new BigDecimal("0.02"); // 2% annual interest
    }

    // Constructor for database loading
    public SavingsAccount(String accountNumber, Customer owner, String branch, BigDecimal balance) {
        super(accountNumber, owner, branch, balance);
        this.interestRate = new BigDecimal("0.02");
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void applyMonthlyInterest() {
        BigDecimal monthlyInterest = balance.multiply(interestRate).divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP);
        deposit(monthlyInterest);
    }
}