package com.banking.model;
import java.math.BigDecimal;

public class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;

    public ChequeAccount(Customer owner, String branch, BigDecimal initialBalance,
                         String employerName, String employerAddress) {
        super(owner, branch, initialBalance);
        this.employerName = employerName;
        this.employerAddress = employerAddress;

        if (employerName == null || employerName.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Employer name is required for Cheque Account");
        }
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("❌ Withdrawal amount must be positive");
        }
        if (amount.compareTo(balance) > 0) {
            throw new IllegalArgumentException("❌ Insufficient funds");
        }
        balance = balance.subtract(amount);
        System.out.println("✅ Withdrawn: BWP " + amount + " from " + accountNumber);
    }

    public String getEmployerName() { return employerName; }
    public String getEmployerAddress() { return employerAddress; }
}