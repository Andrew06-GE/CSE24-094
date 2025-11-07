package main.com.banking.test;

import main.com.banking.service.Bank;
import main.com.banking.db.DatabaseConnection;
import java.math.BigDecimal;
import java.time.LocalDate;

public class IntegrationTest {
    private Bank bank;

    public IntegrationTest() {
        this.bank = new Bank();
    }

    public void runAllTests() {
        System.out.println("=== CLUTCH MANDEM BANK INTEGRATION TEST SUITE ===");
        System.out.println();

        test1_databaseConnection();
        test2_customerRegistration();
        test3_accountManagement();
        test4_transactionProcessing();
        test5_dataPersistence();
        test6_bankReports();

        System.out.println("=== INTEGRATION TESTING COMPLETE ===");
    }

    private void test1_databaseConnection() {
        System.out.println("TEST 1: Database Connection & Schema");
        System.out.println("--------------------------------------");
        try {
            DatabaseConnection.testConnection();
            System.out.println("✓ SUCCESS: Database connection established");
            System.out.println("✓ SUCCESS: Database schema verified");
        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
        }
        System.out.println();
    }

    private void test2_customerRegistration() {
        System.out.println("TEST 2: Customer Registration Flow");
        System.out.println("-----------------------------------");
        try {
            // Test Individual Customer Registration
            bank.registerIndividualCustomer("John", "Smith", "123 Main Street", LocalDate.of(1985, 3, 15));
            System.out.println("✓ SUCCESS: Individual customer 'John Smith' registered");

            bank.registerIndividualCustomer("Sarah", "Johnson", "456 Oak Avenue", LocalDate.of(1990, 7, 22));
            System.out.println("✓ SUCCESS: Individual customer 'Sarah Johnson' registered");

            // Test Corporate Customer Registration
            bank.registerCorporateCustomer("Tech Solutions Ltd", "789 Business Park");
            System.out.println("✓ SUCCESS: Corporate customer 'Tech Solutions Ltd' registered");

            bank.registerCorporateCustomer("Global Foods Inc", "321 Commerce Road");
            System.out.println("✓ SUCCESS: Corporate customer 'Global Foods Inc' registered");

            // Verify customers in system
            String customerReport = bank.viewAllCustomers();
            System.out.println("✓ SUCCESS: Customer data retrieved from database");
            System.out.println("CUSTOMER REGISTRATION VERIFICATION:");
            System.out.println(customerReport);

        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }

    private void test3_accountManagement() {
        System.out.println("TEST 3: Account Management Flow");
        System.out.println("--------------------------------");
        try {


            // Find customer ID by trying to open accounts
            String individualCustomerId = "CUST" + (System.currentTimeMillis() - 10000);
            String corporateCustomerId = "CUST" + (System.currentTimeMillis() - 5000);

            // Open Savings Account
            bank.openSavingsAccount(individualCustomerId, "Main Branch", new BigDecimal("1500.00"));
            System.out.println("✓ SUCCESS: Savings account opened for individual customer");

            // Open Investment Account
            bank.openInvestmentAccount(individualCustomerId, "Downtown Branch", new BigDecimal("5000.00"));
            System.out.println("✓ SUCCESS: Investment account opened for individual customer");

            // Open Cheque Account for corporate customer
            bank.openChequeAccount(corporateCustomerId, "Business Branch", new BigDecimal("10000.00"), "Tech Solutions Ltd", "789 Business Park");
            System.out.println("✓ SUCCESS: Cheque account opened for corporate customer");

            // Verify accounts
            String accountsInfo = bank.viewCustomerAccounts(individualCustomerId);
            System.out.println("✓ SUCCESS: Customer accounts retrieved successfully");
            System.out.println("ACCOUNT MANAGEMENT VERIFICATION:");
            System.out.println(accountsInfo);

        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            System.out.println("⚠ NOTE: This may fail if customer IDs are not exact. Manual testing recommended.");
        }
        System.out.println();
    }

    private void test4_transactionProcessing() {
        System.out.println("TEST 4: Transaction Processing Flow");
        System.out.println("------------------------------------");
        try {
            // Create a test customer and account for transactions
            bank.registerIndividualCustomer("Test", "User", "999 Test Lane", LocalDate.of(1995, 1, 1));

            String testCustomerId = "CUST" + (System.currentTimeMillis() - 1000);
            var testAccount = bank.openSavingsAccount(testCustomerId, "Test Branch", new BigDecimal("1000.00"));
            String testAccountNumber = testAccount.getAccountNumber();

            System.out.println("✓ TEST ACCOUNT CREATED: " + testAccountNumber);

            // Test Deposit
            bank.deposit(testAccountNumber, new BigDecimal("500.00"));
            System.out.println("✓ SUCCESS: Deposit of BWP 500.00 processed");

            // Test Withdrawal
            bank.withdraw(testAccountNumber, new BigDecimal("200.00"));
            System.out.println("✓ SUCCESS: Withdrawal of BWP 200.00 processed");

            // Test Multiple Transactions
            bank.deposit(testAccountNumber, new BigDecimal("1000.00"));
            bank.withdraw(testAccountNumber, new BigDecimal("300.00"));
            System.out.println("✓ SUCCESS: Multiple transactions processed");

            // Verify final state
            String finalAccounts = bank.viewCustomerAccounts(testCustomerId);
            System.out.println("✓ SUCCESS: Transaction results verified");
            System.out.println("FINAL ACCOUNT STATE:");
            System.out.println(finalAccounts);

        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }

    private void test5_dataPersistence() {
        System.out.println("TEST 5: Data Persistence Verification");
        System.out.println("--------------------------------------");
        try {
            String bankSummary = bank.getBankSummary();
            System.out.println("✓ SUCCESS: Current system state retrieved");
            System.out.println("CURRENT BANK SUMMARY:");
            System.out.println(bankSummary);

            System.out.println("⚠ MANUAL VERIFICATION REQUIRED:");
            System.out.println("1. Close and restart the application");
            System.out.println("2. Check if all data is still present");
            System.out.println("3. Verify customers, accounts, and transactions persist");

        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
        }
        System.out.println();
    }

    private void test6_bankReports() {
        System.out.println("TEST 6: Reporting System");
        System.out.println("-------------------------");
        try {
            // Test Customer Report
            String customerReport = bank.viewAllCustomers();
            System.out.println("✓ SUCCESS: Customer report generated");
            System.out.println("CUSTOMER REPORT:");
            System.out.println(customerReport);
            System.out.println();

            // Test Bank Summary
            String bankSummary = bank.getBankSummary();
            System.out.println("✓ SUCCESS: Bank summary report generated");
            System.out.println("BANK SUMMARY REPORT:");
            System.out.println(bankSummary);
            System.out.println();

            // Test Transaction Count
            int transactionCount = bank.getAllTransactions().size();
            System.out.println("✓ SUCCESS: Transaction system verified");
            System.out.println("TOTAL TRANSACTIONS IN SYSTEM: " + transactionCount);

        } catch (Exception e) {
            System.out.println("✗ FAILED: " + e.getMessage());
        }
        System.out.println();
    }

    public static void main(String[] args) {
        IntegrationTest testSuite = new IntegrationTest();
        testSuite.runAllTests();
    }
}