package com.banking.model;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class InvestmentAccount extends Account implements InterestBearing {
    public static final BigDecimal MIN_OPENING_BALANCE = new BigDecimal("500.00");

    public InvestmentAccount(Customer owner, String branch, BigDecimal initialBalance) {
        super(owner, branch, initialBalance);
        if (initialBalance.compareTo(MIN_OPENING_BALANCE) < 0) {
            throw new IllegalArgumentException(" Investment account requires minimum BWP " + MIN_OPENING_BALANCE);
        }
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(" Withdrawal amount must be positive");
        }
        if (amount.compareTo(balance) > 0) {
            throw new IllegalArgumentException(" Insufficient funds");
        }
        balance = balance.subtract(amount);
        System.out.println(" Withdrawn: BWP " + amount + " from " + accountNumber);
    }

    @Override
    public void applyMonthlyInterest() {
        BigDecimal rate = getMonthlyInterestRate();
        BigDecimal interest = balance.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        balance = balance.add(interest);
        System.out.println(" Applied " + rate.multiply(new BigDecimal("100")) + "% interest: +BWP " + interest + " to " + accountNumber);
    }

    @Override
    public BigDecimal getMonthlyInterestRate() {
        return owner.isCorporate() ? new BigDecimal("0.075") : new BigDecimal("0.025");
    }
}