package main.com.banking.model;

import java.math.BigDecimal;

public class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;

    public ChequeAccount(Customer owner, String branch, BigDecimal balance, String employerName, String employerAddress) {
        super(owner, branch, balance);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    // Constructor for database loading
    public ChequeAccount(String accountNumber, Customer owner, String branch, BigDecimal balance,
                         String employerName, String employerAddress) {
        super(accountNumber, owner, branch, balance);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    public String getEmployerName() {
        return employerName;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }
}