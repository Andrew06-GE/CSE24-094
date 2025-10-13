package com.banking.model;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SavingsAccount extends Account implements InterestBearing {

    public SavingsAccount(Customer owner, String branch, BigDecimal initialBalance) {
        super(owner, branch, initialBalance);
    }

    @Override
    public void withdraw(BigDecimal amount) {
        throw new UnsupportedOperationException(" Withdrawals are not allowed from Savings Account");
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
        return owner.isCorporate() ? new BigDecimal("0.00075") : new BigDecimal("0.00025");
    }
}