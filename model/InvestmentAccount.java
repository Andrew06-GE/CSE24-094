package main.com.banking.model;

import java.math.BigDecimal;

public class InvestmentAccount extends Account {
    public static final BigDecimal MIN_OPENING_BALANCE = new BigDecimal("1000.00");
    private BigDecimal interestRate;

    public InvestmentAccount(Customer owner, String branch, BigDecimal balance) {
        super(owner, branch, balance);
        this.interestRate = new BigDecimal("0.035"); // 3.5% annual interest
    }

    // Constructor for database loading
    public InvestmentAccount(String accountNumber, Customer owner, String branch, BigDecimal balance) {
        super(accountNumber, owner, branch, balance);
        this.interestRate = new BigDecimal("0.035");
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void applyMonthlyInterest() {
        BigDecimal monthlyInterest = balance.multiply(interestRate).divide(new BigDecimal("12"), 2, BigDecimal.ROUND_HALF_UP);
        deposit(monthlyInterest);
    }
}