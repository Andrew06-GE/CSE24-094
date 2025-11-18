package main.com.banking.model;

import java.time.LocalDate;

public class Customer {
    private String customerId;
    private String firstName;
    private String surname;
    private String companyName;
    private String address;
    private LocalDate dateOfBirth;
    private boolean corporate;

    // Constructors
    public Customer(String firstName, String surname, String address, LocalDate dateOfBirth) {
        this.customerId = generateCustomerId();
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.corporate = false;
    }

    public Customer(String companyName, String address) {
        this.customerId = generateCustomerId();
        this.companyName = companyName;
        this.address = address;
        this.corporate = true;
    }

    // Add this constructor for database loading
    public Customer(String customerId, String firstName, String surname, String companyName,
                    String address, LocalDate dateOfBirth, boolean corporate) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.companyName = companyName;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.corporate = corporate;
    }

    private String generateCustomerId() {
        return "CUST" + System.currentTimeMillis();
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public String getFirstName() { return firstName; }
    public String getSurname() { return surname; }
    public String getCompanyName() { return companyName; }
    public String getAddress() { return address; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public boolean isCorporate() { return corporate; }

    public String getDisplayName() {
        return corporate ? companyName : firstName + " " + surname;
    }

    public String getFullName() {
        return corporate ? companyName : firstName + " " + surname;
    }
}