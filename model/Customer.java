package com.banking.model;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private String customerId;
    private String firstName;
    private String surname;
    private String address;
    private LocalDate dateOfBirth;
    private boolean isCorporate;
    private String companyName;
    private String companyAddress;
    private List<Account> accounts;

    
    public Customer(String firstName, String surname, String address, LocalDate dateOfBirth) {
        this.customerId = "CUST" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.isCorporate = false;
        this.accounts = new ArrayList<>();
    }

  
    public Customer(String companyName, String companyAddress) {
        this.customerId = "CORP" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.isCorporate = true;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getFullName() { return firstName + " " + surname; }
    public String getAddress() { return address; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public boolean isCorporate() { return isCorporate; }
    public String getCompanyName() { return companyName; }
    public String getCompanyAddress() { return companyAddress; }
    public List<Account> getAccounts() { return new ArrayList<>(accounts); }

    public String getDisplayName() {
        return isCorporate ? companyName : firstName + " " + surname;
    }
}