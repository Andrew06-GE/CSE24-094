package com.banking.service;

import com.banking.model.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

public class Bank {
    private String name;
    private Map<String, Customer> customers;
    private Map<String, Account> accounts;

    public Bank(String name) {
        this.name = name;
        this.customers = new HashMap<>();
        this.accounts = new HashMap<>();
        System.out.println(name + " is now open!");
    }

    
    public Customer registerIndividualCustomer(String firstName, String surname, String address, LocalDate dateOfBirth) {
        Customer customer = new Customer(firstName, surname, address, dateOfBirth);
        customers.put(customer.getCustomerId(), customer);
        System.out.println("Registered individual customer: " + customer.getFullName() + " (ID: " + customer.getCustomerId() + ")");
        return customer;
    }

    
    public Customer registerCorporateCustomer(String companyName, String companyAddress) {
        Customer customer = new Customer(companyName, companyAddress);
        customers.put(customer.getCustomerId(), customer);
        System.out.println("Registered corporate customer: " + companyName + " (ID: " + customer.getCustomerId() + ")");
        return customer;
    }

    
    public Account openSavingsAccount(String customerId, String branch, BigDecimal initialDeposit) {
        Customer customer = getCustomerOrThrow(customerId);
        SavingsAccount account = new SavingsAccount(customer, branch, initialDeposit);
        customer.addAccount(account);
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Opened Savings Account " + account.getAccountNumber() + " for " + customer.getDisplayName());
        return account;
    }

    
    public Account openInvestmentAccount(String customerId, String branch, BigDecimal initialDeposit) {
        Customer customer = getCustomerOrThrow(customerId);

        if (initialDeposit.compareTo(InvestmentAccount.MIN_OPENING_BALANCE) < 0) {
            throw new IllegalArgumentException("Investment account requires minimum BWP " + InvestmentAccount.MIN_OPENING_BALANCE);
        }

        InvestmentAccount account = new InvestmentAccount(customer, branch, initialDeposit);
        customer.addAccount(account);
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Opened Investment Account " + account.getAccountNumber() + " for " + customer.getDisplayName());
        return account;
    }

    
    public Account openChequeAccount(String customerId, String branch, BigDecimal initialDeposit,
                                     String employerName, String employerAddress) {
        Customer customer = getCustomerOrThrow(customerId);

        if (employerName == null || employerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Employer information is required for Cheque Account");
        }

        ChequeAccount account = new ChequeAccount(customer, branch, initialDeposit, employerName, employerAddress);
        customer.addAccount(account);
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Opened Cheque Account " + account.getAccountNumber() + " for " + customer.getDisplayName());
        return account;
    }

    
    public void deposit(String accountNumber, BigDecimal amount) {
        Account account = getAccountOrThrow(accountNumber);
        account.deposit(amount);
    }

    
    public void withdraw(String accountNumber, BigDecimal amount) {
        Account account = getAccountOrThrow(accountNumber);
        account.withdraw(amount);
    }

    
    public void applyMonthlyInterestToAll() {
        System.out.println("APPLYING MONTHLY INTEREST TO ALL ACCOUNTS...");
        int count = 0;
        for (Account account : accounts.values()) {
            if (account instanceof InterestBearing) {
                ((InterestBearing) account).applyMonthlyInterest();
                count++;
            }
        }
        System.out.println("Monthly interest applied to " + count + " accounts");
    }
    
    public void viewCustomerAccounts(String customerId) {
        Customer customer = getCustomerOrThrow(customerId);
        System.out.println("ACCOUNTS FOR: " + customer.getDisplayName() + " (Customer ID: " + customerId + ")");

        if (customer.getAccounts().isEmpty()) {
            System.out.println("   No accounts found");
            return;
        }

        for (Account account : customer.getAccounts()) {
            String type = account.getClass().getSimpleName();
            String interestInfo = (account instanceof InterestBearing) ? " (Earns Interest)" : "";
            System.out.println("   Account " + account.getAccountNumber() + " - " + type + ": BWP " + account.getBalance() + interestInfo);
        }
    }

    public void viewAllCustomers() {
        System.out.println("ALL CUSTOMERS:");
        for (Customer customer : customers.values()) {
            String type = customer.isCorporate() ? "Company" : "Individual";
            System.out.println("   Customer ID: " + customer.getCustomerId() + " - " + customer.getDisplayName() + " (" + type + ")");

            // Show accounts for this customer
            if (!customer.getAccounts().isEmpty()) {
                for (Account account : customer.getAccounts()) {
                    System.out.println("      Account " + account.getAccountNumber() + " - " + account.getClass().getSimpleName());
                }
            }
        }
    }

    
    public void viewAllAccounts() {
        System.out.println("ALL ACCOUNTS IN BANK:");
        for (Account account : accounts.values()) {
            String customerName = account.getOwner().getDisplayName();
            String type = account.getClass().getSimpleName();
            System.out.println("   Account " + account.getAccountNumber() + " - " + type + " - Owner: " + customerName + " - Balance: BWP " + account.getBalance());
        }
    }

    
    private Customer getCustomerOrThrow(String customerId) {
        Customer customer = customers.get(customerId);
        if (customer == null) throw new IllegalArgumentException("Customer not found: " + customerId);
        return customer;
    }

    private Account getAccountOrThrow(String accountNumber) {
        Account account = accounts.get(accountNumber);
        if (account == null) throw new IllegalArgumentException("Account not found: " + accountNumber);
        return account;
    }

    
    public Customer getCustomer(String customerId) { return customers.get(customerId); }
    public Account getAccount(String accountNumber) { return accounts.get(accountNumber); }
    public Collection<Customer> getAllCustomers() { return customers.values(); }
    public String getName() { return name; }
}