package main.com.banking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private String accountNumber;
    private String type;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private LocalDateTime timestamp;

    // Constructor for new transactions
    public Transaction(String accountNumber, String type, BigDecimal amount, BigDecimal balanceAfter) {
        this.transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    // Constructor for database loading
    public Transaction(String transactionId, String accountNumber, String type, BigDecimal amount, BigDecimal balanceAfter) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s BWP %.2f | Balance: BWP %.2f",
                timestamp, type, accountNumber, amount, balanceAfter);
    }
}